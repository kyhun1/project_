package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMemberRepository implements MemberRepository {

    // DB 붙기 위해 data source 필요
    private final DataSource dataSource;

    // spring을 통해 주입 받아야 한다. -> application.properties의 코드를 받기위해
    public JdbcMemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
        // 데이터 베이스를 받을 수 있는 소켓정보 datasource를 받을 수 있는 명령어
        // sql을 통해 받을 수 있다.
        // dataSource.getConnection()
    }

    @Override
    public Member save(Member member) {
        // 파라미터 바인딩을 하기 위해 아래와 같은 명령어
        String sql = "insert into member(name) values(?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        // ResultSet : 결과를 받는 곳

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // RETURN_GENERATED_KEYS : DB에 인서트 하면 id값을 가져오기 위한 설정

            pstmt.setString(1, member.getName());

            pstmt.executeUpdate();
            // insert 쿼리가 DB에 날라감
            rs = pstmt.getGeneratedKeys();
            // getGeneratedKeys / RETURN_GENERATED_KEYS에 매칭 되서 작동 ex) id 번호

            if (rs.next()) {
                member.setId(rs.getLong(1));
            } else {
                throw new SQLException("id 조회 실패");
            }
            return member;
        } catch (Exception e) {
            throw new IllegalStateException(e);
            // 데이터 커넥션 끝난 후 외부 DB와의 인서트 내용 리소스를 제거 해줘야 한다.
            // 제거를 하지 않을 경우 인서트 된 내용이 남아 에러를 발생 하게 된다.
        } finally {
            close(conn, pstmt, rs);
        }
    }

    // 조회 부분
    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select * from member where id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);

            rs = pstmt.executeQuery();
            // 조회는 executeQuery

            if (rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "select * from member";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            List<Member> members = new ArrayList<>();
            while (rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                members.add(member);
            }

            return members;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findByName(String name) {
        String sql = "select * from member where name = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            }
            return Optional.empty();

        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    // spring 프레임 워크를 이용해서 데이터를 가져올 떄는 DataSourceUtils를 이용해서 데이터를 가져와야 한다.
    // 이전의 트랜젝션에 걸릴 수 있기 때문이다. 데이터베이트 트랙제션에 걸릴 수 있기 떄문에 변하지 않기 위해서
    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
        // 닫을 떄도  DataSourceUtils 를 이용해서 릴리즈 해줘야 한다.
    }
}