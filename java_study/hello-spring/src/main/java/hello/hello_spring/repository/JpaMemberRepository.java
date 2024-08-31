package hello.hello_spring.repository;

import hello.hello_spring.domain.Member;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;
    // jpa는 entity로 모든 작동을 하게 된다.

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    // id 저장
    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    // memeber 조회
    @Override
    public Optional<Member> findById(Long id) {
        Member memeber = em.find(Member.class, id);
        return Optional.ofNullable(memeber);
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name" , name)
                .getResultList();

        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Memeber m", Member.class)
                .getResultList();
    }
}
