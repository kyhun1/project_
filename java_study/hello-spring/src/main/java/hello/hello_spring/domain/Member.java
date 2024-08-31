package hello.hello_spring.domain;


import jakarta.persistence.*;

@Entity
public class Member {

    // id 식별자 필요 - 데이터 베이스에 저장하기 위해 필요 단순시퀀스 데이터저장을 위한 id //
    // name 임의값  //
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
