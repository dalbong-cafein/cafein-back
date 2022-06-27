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
    public List<Report> findByReportToday() {

        return queryFactory.select(report)
                .from(report)
                .leftJoin(report.toMember).fetchJoin()
                .where(report.regDateTime.between(LocalDateTime.now().minusDays(1).toLocalDate().atStartOfDay(), LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(23, 59, 59))))
                .fetch();
    }
}
