package com.dalbong.cafein.domain.report;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.QMember;
import com.dalbong.cafein.domain.review.QReview;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.dalbong.cafein.domain.member.QMember.member;
import static com.dalbong.cafein.domain.report.QReport.report;
import static com.dalbong.cafein.domain.review.QReview.review;

public class ReportRepositoryImpl implements ReportRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    public ReportRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 금일 신고 회원
     */
    @Override
    public List<Object[]> findByReportToday() {

        List<Tuple> result = queryFactory.select(report, member)
                .from(report)
                .leftJoin(review).on(review.reviewId.eq(report.review.reviewId))
                .leftJoin(member).on(member.memberId.eq(review.member.memberId))
                .where(report.regDateTime.between(LocalDateTime.now().minusDays(1).toLocalDate().atStartOfDay(),
                        LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(23, 59, 59))))
                .fetch();

        return result.stream().map(t -> t.toArray()).collect(Collectors.toList());
    }

    /**
     * 회원 정지기간 확인 후 일반 회원 상태로 변경
     */
    @Override
    public List<Member> findMemberByReportExpiredToday() {
        return null;
    }
}
