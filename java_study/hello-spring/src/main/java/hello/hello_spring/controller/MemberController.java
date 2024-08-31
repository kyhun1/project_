package hello.hello_spring.controller;

import hello.hello_spring.domain.Member;
import hello.hello_spring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MemberController {

    // new memberService를 생성하면 다른 여러 contrller들이 memberSerivce를 사용하게 된다.
    // ex) 주문 controller에서 memberService를 사용 할 수 있고 여러 다른 controller에서 사용 할 수 있다.
    // -> 하나만 생성하고 고용으로 생성 하면 되기에 spring container에 등록 하고 사용하면된다.
    //private final MemberService memberService = new MemberService();

    private final MemberService memberService;

    // 스프링 컨테이너에 생성
    // DI 주입
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
        System.out.println("memberSerivce = " + memberService.getClass());
    }

    @GetMapping("/members/new")
    // home.html에서 /members/new로 넘어가기 떄문에
    public String createForm(){
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String crate(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());

        memberService.join(member);

        //System.out.println("member = " + member.getName());
        //getName을 통해 데이터가 입력된 데이트를 확인 할 수 있다.

        return "redirect:/";
        // redirect: 홈 화면으로 보내는 단어
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }


}

// 회원가입 -> members/new에 들어가게 된다. -> url에 직접 입력 되는 것을 get 방식
// ->members/creteMemberForm을 호출 하기에 templates의 members/creteMemberForm 을 찾게된다.
// 뷰리저브라는 친구 떄문에 templages의 members/creteMemberForm 선택되게 되고 랜더링을 하게 된다.
// 안에 form name은 서버에서 가저올떄 name
// postmapping -> 데이터를 form 형식으로 저장 할 때 쓰는 형식
// url은 동일 하지만 psot9데이터 등록) / get(조회) 방식에 따라 달라진다
// create을 통해 호출이 된다.
// spring이 setName을 이용해서 저장 -> getName을 통해 데이터를 출력