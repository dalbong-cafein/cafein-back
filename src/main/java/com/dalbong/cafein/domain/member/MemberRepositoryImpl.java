package com.dalbong.cafein.domain.member;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.Optional;

import static com.dalbong.cafein.domain.member.QMember.member;

public class MemberRepositoryImpl implements MemberRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 카카오 계정 기존 회원 찾기
     */
    @Override
    public Optional<Member> findByKakaoIdAndNotDeleted(String kakaoId) {

        Member findMember = queryFactory.selectFrom(member)
                .join(member.roleSet)
                .where(member.kakaoId.eq(kakaoId), member.isDeleted.isFalse().or(QMember.member.isDeleted.isNull()))
                .fetchOne();
        return findMember != null ? Optional.of(findMember) : Optional.empty();
    }

    /**
     * 네이버 계정 기존 회원 찾기
     */
    @Override
    public Optional<Member> findByNaverIdAndNotDeleted(String naverId) {

        Member findMember = queryFactory.selectFrom(member)
                .join(member.roleSet)
                .where(member.naverId.eq(naverId), member.isDeleted.isFalse().or(QMember.member.isDeleted.isNull()))
                .fetchOne();
        return findMember != null ? Optional.of(findMember) : Optional.empty();
    }
}
