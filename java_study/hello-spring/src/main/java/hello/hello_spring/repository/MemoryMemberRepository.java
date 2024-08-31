package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;


public class MemoryMemberRepository implements MemberRepository{

    private static Map<Long, Member> store = new HashMap<>();
    // Map<키 : 여기서 회원 아이디, 값은 Member
    private static long sequence = 0L;
    // sequence : 0,1,2 키값을 생성 해주는것

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
        // memberRepository 에서 고객id 내용이 store를 이용하여 map에 저장 //
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
        // 추출했을떄 null 값이 반환 될 가능성이 있기 떄문에  optional를 이용
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
        //store 속의 values member가 추출
        //검증을 하기 위해서 test case
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }
    //filter equals 떄문에 member 내에서 name이 동일 할 경우에만 추출
    //findAny loop를 돌면서 하나라도 동일한게 확인 되면 null 값으로 반환


    public void clearStore(){
        store.clear();
        // store를 싹 비우는 역활
    }
}
