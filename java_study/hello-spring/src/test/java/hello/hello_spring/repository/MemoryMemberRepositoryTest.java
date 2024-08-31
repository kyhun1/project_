package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }
    // 각각의 test가 실행 되고 끝날떄 마다 리포지스토리 삭제

    @Test
    public void save() {
        Member member = new Member();
        member.setName("spring");

        repository.save(member);

        Member result = repository.findById(member.getId()).get();
        assertThat(member).isEqualTo(result);
        // Assertions.assertEquals(member, null);

        // new 에서 입력 된 단어와 DB에서 추출한 내용이 동일한지 확인 하는 코드
        // System.out.println("result = " + (result = member));

    }

    //이름으로 찾는 부분 Test

    @Test
    public void findByName(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get();

        assertThat(result).isEqualTo(member1);

    }

    @Test
    public  void findAll(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring1");
        repository.save(member2);

        List<Member> result = repository.findAll();

        assertThat(result.size()).isEqualTo(2);

        //findAll 에서 2라는 값을 넣었을 떄는 문제 없이 되는것을 확인 할 수 있다.
        //하지만 class에서 돌리면 error가 발생하는 것을 확인 할 수 있다.
        //why?
        //순서와 상관없이 메서드는 따로 작동을 하기 떄문에 class를 실행했을 떄 error 발생
        //findALl이 먼저 실행되면 -> 먼저 실행 된 부분에서 입렫 된 값으로 테스트를 할 수 있기에
        //안전 장치를 만들어야 한다.
        //각각의 리포지토리를 하고 clear 했다는 코드를 입력해 줘야 한다.
        //AfterEach 각각의 리포지토리를 작동 후 종료후에 콜백 메세지를 보내달라는 명령어
        //테스트는 서로간의 의존관계 없이 작성 되어야 한다.
        //하지만 기존의 내용이 남겨져 있기에 afterEach라는 명령어로 각각의 테스트 후
        //store안에 남겨져 있는 내역을 지우고 다시 테스트를 할 수 있게 하기 위해
        //장치를 해두어야 class 에서 발동을 했을 때 문제 없이 작동하는 것을 확인 할 수 있다.
        //검증 할 수 있는 틀을 만들고 그 안에 내용이 들어가는지 확인 하는 방법을 TDD
    }

}
