package com.dalbong.cafein.domain.member;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long>, MemberRepositoryQuerydsl {

    @Query("select m, mi from Member m left join MemberImage mi on mi.member.memberId = m.memberId " +
            "where m.memberId = :memberId")
    List<Object[]> getMemberInfo(@Param("memberId") Long memberId);

    Optional<Member> findByEmail(String email);

    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Member> findWithRoleSetByMemberId(Long memberId);

    Optional<Member> findByNickname(String nickname);
}
