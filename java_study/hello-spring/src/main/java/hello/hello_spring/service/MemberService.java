package hello.hello_spring.service;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class MemberService {

    //private final MemberRepository memberRepository = new MemoryMemberRepository();
    //test와 같은 인스턴스를 쓰게 하기 위해 위의 내용에서 아래의 내용으로 변경
    private final MemberRepository memberRepository;

    // 외부에서 입력을 받을 수 있게 변경
    // DI : Test에서 생성한 내용을 받을 수 있게 아래와 같이 코드를 변경한 것


    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원가입
     */

    public  Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

//        AOP를 이용하지 않고 각각 실행 했을 떄 시간 확인 하기 윈한 코드
//        Long start = System.currentTimeMillis();

//        try {
//        validateDuplicateMember(member); // 중복 회원 검증
//        memberRepository.save(member);
//        return member.getId();
//            validateDuplicateMember(member); // 중복 회원 검증
//            memberRepository.save(member);
//            return member.getId();
//        } finally {
//            long finish = System.currentTimeMillis();
//            long timeMs = finish - start;
//            System.out.println("join = " + timeMs + "ms");
//        }



    private void validateDuplicateMember(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }


    /**
     * 전체 회원 조회
     */

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
//        long start = System.currentTimeMillis();
//        try {
//        return memberRepository.findAll();
//        } finally {
//            long finish = System.currentTimeMillis();
//            long timeMs = finish - start;
//            System.out.println("findMembers = " + timeMs + "ms");
//        }

    // findAIll 부분은 repository 부분에서 List<Member> findAll();
    // List 형식으로 추출 된다.

    public Optional<Member> findOne(Long memberId) {
        return memberRepository.findById(memberId);
    }
    // 회원 가입 할 떄 중복이름이 있으면 문제가 생기는지 확인 하기 위해
    // 회원 서비스 테스트 케이스를 작성

}

/**
 * serivce 부분은 join 같은 명령어가 들어가기 때문에 비즈니스적 역활을 가진다.
 * 고로 비즈니스적 문제가 생겼을 때 serivce 패키지를 확인 한다.
 * repository 단순히 개발스럽게 데이터를 input/output 부분을 담당한다.
 */
