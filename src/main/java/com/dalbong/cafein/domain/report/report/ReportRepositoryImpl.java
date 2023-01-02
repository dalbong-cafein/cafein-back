package com.dalbong.cafein.domain.report.report;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dalbong.cafein.domain.memo.QReportMemo.reportMemo;
import static com.dalbong.cafein.domain.report.report.QReport.report;
import static org.aspectj.util.LangUtil.isEmpty;

public class ReportRepositoryImpl implements ReportRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    public ReportRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 중복 신고 확인
     */
    @Override
    public boolean existReport(Long reviewId, Long principalId) {

        Integer fetchOne = queryFactory.selectOne()
                .from(report)
                .where(report.review.reviewId.eq(reviewId),
                        report.fromMember.memberId.eq(principalId))
                .fetchOne();

        return fetchOne != null;
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

    /**
     * 신고 단일 조회 - fetch ToMember
     */
    @Override
    public Optional<Report> findWithToMemberById(Long reportId) {

        Report findReport = queryFactory.select(report)
                .from(report)
                .join(report.toMember).fetchJoin()
                .where(report.reportId.eq(reportId))
                .fetchOne();

        return findReport != null ? Optional.of(findReport) : Optional.empty();
    }

    /**
     * 관리자단 신고 리스트 조회
     */
    @Override
    public Page<Object[]> getReportListOfAdmin(String[] searchType, String keyword, Pageable pageable) {

        JPAQuery<Tuple> query = queryFactory.select(report, reportMemo.memoId)
                .from(report)
                .leftJoin(report.review).fetchJoin()
                .leftJoin(report.toMember).fetchJoin()
                .leftJoin(report.fromMember).fetchJoin()
                .join(report.reportCategory).fetchJoin()
                .leftJoin(reportMemo).on(reportMemo.report.eq(report))
                .where(searchKeyword(searchType, keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        //정렬
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(Report.class, "report");
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<Tuple> results = query.fetch();

        //count 쿼리
        JPAQuery<Report> countQuery = queryFactory
                .select(report)
                .from(report)
                .where(searchKeyword(searchType, keyword));

        List<Object[]> content = results.stream().map(t -> t.toArray()).collect(Collectors.toList());

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());
    }

    /**
     * 관리자단 신고 리스트 사용자 개수 지정 조회
     */
    @Override
    public List<Report> getCustomLimitReportListOfAdmin(int limit) {

        return queryFactory.select(report)
                .from(report)
                .leftJoin(report.toMember).fetchJoin()
                .leftJoin(report.fromMember).fetchJoin()
                .join(report.reportCategory).fetchJoin()
                .orderBy(report.reportId.desc())
                .limit(limit)
                .fetch();
    }

    private BooleanBuilder searchKeyword(String[] searchType, String keyword) {

        BooleanBuilder builder = new BooleanBuilder();

        if (searchType != null){ ;
            for(String t : searchType){
                switch (t){
                    case "rp":
                        builder.or(containReportId(keyword));
                        break;
                    case "m":
                        builder.or(containMemberId(keyword));
                        break;
                    case "r":
                        builder.or(containReviewId(keyword));
                }
            }
        }
        return builder;
    }

    private BooleanExpression containReportId(String keyword) {

        Long reportId = null;

        try{
            reportId = Long.parseLong(keyword);
        }catch (NumberFormatException e){
            e.getMessage();
        }

        return !isEmpty(keyword) && reportId != null ? report.reportId.eq(reportId) : null;
    }

    private BooleanExpression containMemberId(String keyword) {

        Long memberId = null;

        try {
            memberId = Long.parseLong(keyword);
        }catch (NumberFormatException e){
            e.getMessage();
        }

        return !isEmpty(keyword) && memberId != null ? report.fromMember.memberId.eq(memberId) : null;

    }

    private BooleanExpression containReviewId(String keyword) {

        Long reviewId = null;

        try {
            reviewId = Long.parseLong(keyword);
        }catch (NumberFormatException e){
            e.getMessage();
        }

        return !isEmpty(keyword) && reviewId != null ? report.review.reviewId.eq(reviewId) : null;

    }

}
