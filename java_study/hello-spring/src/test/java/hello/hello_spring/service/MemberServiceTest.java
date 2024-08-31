package hello.hello_spring.service;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class MemberServiceTest {

    // test에서는 한글을 사용해도 상관없음

    //MemberService memberService = new MemberService();
    // clear를 하기위해
    //MemoryMemberRepository memberRepository = new MemoryMemberRepository();

    // 테스트를 실행 할 때마다 각각 생성을 해주게 된다.
    //
    MemberService memberService;
    MemoryMemberRepository memberRepository;

    @BeforeEach
    public void beforeEach(){
        memberRepository = new MemoryMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @AfterEach
    public void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void 회원가입() {
        //given (어떠한 일이 시작됬을 때)
        Member member = new Member();
        member.setName("spring");

        //when (검증)
        Long saveId = memberService.join(member);

        //then (결과)
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());

    }
    // 예외 flow가 중요함 -> join 에서 이름이 중복되서 eroor가 발생하는 것을 확인 해야하기 때문에
    // 코드 작성

    @Test
    public void 중복_회원_예외 () {

        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);

        // 첫번쨰 join에서는 문제가 없이 join 되어야 함
        // 두번쨰 join 부분에서는 -> main -> MemberSerivce 부분에서
        // validateDuplicatrion 부분 때문에 에러가 발생해야 한다.

        // 첫번쨰 방안
//        try {
//            memberService.join(member2);
//            fail();
//        } catch (IllegalStateException e) {
//            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
//        }

        // 아래의 내용을 검증 하기 위한 두번쨰 방안
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

        //then
    }


    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}
// 각각의 자바 내에서 명령어 작동 하는 테스트를 단위 테스트