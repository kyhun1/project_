package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    //회원 id로 찾는 부분 생성//
    Optional<Member> findByName(String name);
    // Optional : findById/findByName 에서 결측값을 null로 반환 되는 방법을 Optional로 반환 자바 8에서 생성
    List<Member> findAll();

}
