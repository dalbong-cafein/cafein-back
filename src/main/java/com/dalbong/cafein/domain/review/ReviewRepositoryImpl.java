package com.dalbong.cafein.domain.review;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dalbong.cafein.domain.image.QMemberImage.memberImage;
import static com.dalbong.cafein.domain.memo.QReviewMemo.reviewMemo;
import static com.dalbong.cafein.domain.review.QReview.review;
import static org.aspectj.util.LangUtil.isEmpty;

public class ReviewRepositoryImpl implements ReviewRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 당일 리뷰 등록 여부 확인
     */
    @Override
    public boolean existRegisterToday(Long storeId, Long memberId) {

        Integer fetchOne = queryFactory.selectOne()
                .from(review)
                .where(review.store.storeId.eq(storeId),
                        review.member.memberId.eq(memberId),
                        review.regDateTime.between(LocalDateTime.now().toLocalDate().atStartOfDay(),
                                LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59))))
                .fetchOne();

        return fetchOne != null;
    }

    /**
     * 가게별 리뷰 리스트 조회
     */
    @Override
    public Page<Object[]> getReviewListOfStore(Long storeId, Boolean isOnlyImage, Pageable pageable) {

        QReview review = new QReview("review");
        QReview reviewSub = new QReview("reviewSub");

        JPAQuery<Tuple> query = queryFactory
                .select(review, memberImage, JPAExpressions
                        .select(reviewSub.count())
                        .from(reviewSub)
                        .where(reviewSub.store.storeId.eq(storeId),
                                reviewSub.member.memberId.eq(review.member.memberId))
                )
                .from(review)
                .leftJoin(review.member).fetchJoin()
                .leftJoin(memberImage).on(memberImage.member.eq(review.member))
                .where(review.store.storeId.eq(storeId), IsOnlyImage(isOnlyImage),
                        review.isPost.isTrue())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        //정렬
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(Review.class, "review");
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<Tuple> results = query.fetch();

        //count 쿼리
        JPAQuery<Review> countQuery = queryFactory
                .select(review)
                .from(review)
                .leftJoin(memberImage).on(memberImage.member.eq(review.member))
                .where(review.store.storeId.eq(storeId), IsOnlyImage(isOnlyImage), review.isPost.isTrue());

        List<Object[]> content = results.stream().map(t -> t.toArray()).collect(Collectors.toList());

        /**
         * 두 경우에는 count 쿼리를 날리지 않음 -> spring Jpa가 지원 (PageableExeutionUtils)
         * 1. 시작 페이지이면서 content 수가 page size보다 작을 경우.
         * 2. 마지막 페이지이면서 content 수가 page size보다 작을 경우.
         * 날릴 필요가 없음.
         */
        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());
    }

    /**
     * 가게별 리뷰 리스트 개수 지정 조회
     */
    @Override
    public List<Object[]> getCustomLimitReviewList(int limit, Long storeId) {

        QReview review = new QReview("review");
        QReview reviewSub = new QReview("reviewSub");

        List<Tuple> results = queryFactory
                .select(review, memberImage, JPAExpressions
                        .select(reviewSub.count())
                        .from(reviewSub)
                        .where(reviewSub.store.storeId.eq(storeId),
                                reviewSub.member.memberId.eq(review.member.memberId))
                )
                .from(review)
                .leftJoin(review.member).fetchJoin()
                .leftJoin(memberImage).on(memberImage.member.eq(review.member))
                .where(review.store.storeId.eq(storeId), review.isPost.isTrue())
                .orderBy(review.reviewId.desc())
                .limit(limit)
                .fetch();

        return results.stream().map(t -> t.toArray()).collect(Collectors.toList());
    }

    /**
     * 회원별 리뷰 리스트 조회
     */
    @Override
    public List<Object[]> getMyReviewList(Long principalId) {

        QReview review = new QReview("review");
        QReview reviewSub = new QReview("reviewSub");

        List<Tuple> result = queryFactory.select(review,
                JPAExpressions.select(reviewSub.count())
                        .from(reviewSub)
                        .where(reviewSub.store.storeId.eq(review.store.storeId),
                                reviewSub.member.memberId.eq(review.member.memberId)))

                .from(review)
                .leftJoin(review.member).fetchJoin()
                .leftJoin(review.store).fetchJoin()
                .where(review.member.memberId.eq(principalId))
                .orderBy(review.reviewId.desc())
                .fetch();

        if(result.isEmpty()) {
            return new ArrayList<>();
        }

        return result.stream().map(t -> t.toArray()).collect(Collectors.toList());
    }

    /**
     * 관리자단 전체 리뷰 리스트 조회
     */
    @Override
    public Page<Object[]> getAllReviewList(String[] searchType, String keyword, Pageable pageable) {

        QReview review = new QReview("review");

        JPAQuery<Tuple> query = queryFactory
                .select(review, reviewMemo.memoId)
                .from(review)
                .leftJoin(review.member).fetchJoin()
                .leftJoin(review.store).fetchJoin()
                .leftJoin(reviewMemo).on(reviewMemo.review.reviewId.eq(review.reviewId))
                .where(searchKeyword(searchType, keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        //정렬
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(Review.class, "review");
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<Tuple> results = query.fetch();

        //count 쿼리
        JPAQuery<Review> countQuery = queryFactory
                .select(review)
                .from(review)
                .where(searchKeyword(searchType, keyword));

        List<Object[]> content = results.stream().map(t -> t.toArray()).collect(Collectors.toList());

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());
    }

    /**
     * 관리자단 리뷰 상세 정보 조회
     */
    @Override
    public Optional<Object[]> getDetailReview(Long reviewId) {

        QReview review = new QReview("review");
        QReview reviewSub = new QReview("reviewSub");

        Tuple tuple = queryFactory.select(review, JPAExpressions.select(reviewSub.member.memberId.count())
                .from(reviewSub)
                .where(reviewSub.store.storeId.eq(review.store.storeId),
                        reviewSub.member.memberId.eq(review.member.memberId))
                .groupBy(reviewSub.member.memberId))
                .from(review)
                .leftJoin(review.member).fetchJoin()
                .leftJoin(review.store).fetchJoin()
                .where(review.reviewId.eq(reviewId))
                .fetchOne();

        return tuple != null ? Optional.ofNullable(tuple.toArray()) : Optional.empty();
    }

    /**
     * 관리자단 회원별 리뷰 리스트 조회
     */
    @Override
    public List<Object[]> getReviewListOfMember(Long memberId) {

        List<Tuple> result = queryFactory.select(review, reviewMemo.memoId)
                .from(review)
                .leftJoin(review.member).fetchJoin()
                .leftJoin(review.store).fetchJoin()
                .leftJoin(reviewMemo).on(reviewMemo.review.reviewId.eq(review.reviewId))
                .where(review.member.memberId.eq(memberId))
                .fetch();

        if(result.isEmpty()) {
            return new ArrayList<>();
        }
        
        return result.stream().map(t -> t.toArray()).collect(Collectors.toList());
    }

    /**
     * 관리자단 오늘 등록된 리뷰 개수 조회
     */
    @Override
    public Long getRegisterCountOfToday() {
        return queryFactory
                .select(review.count())
                .from(review)
                .where(review.regDateTime.between(LocalDateTime.now().toLocalDate().atStartOfDay(),
                        LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59))))
                .fetchOne();
    }

    private  BooleanBuilder searchKeyword(String[] searchType, String keyword) {

        BooleanBuilder builder = new BooleanBuilder();

        if (searchType != null){ ;
            for(String t : searchType){
                switch (t){
                    case "r":
                        builder.or(containReviewId(keyword));
                        break;
                    case "w":
                        builder.or(containMemberId(keyword));
                        break;
                    case "s":
                        builder.or(containStoreId(keyword));
                }
            }
        }
        return builder;
    }

    private BooleanExpression containReviewId(String keyword) {

        Long reviewId = null;

        try{
            reviewId = Long.parseLong(keyword);
        }catch (NumberFormatException e){
                e.getMessage();
        }

        return !isEmpty(keyword) && reviewId != null ? review.reviewId.eq(reviewId) : null;
    }

    private BooleanExpression containMemberId(String keyword) {

        Long memberId = null;

        try {
            memberId = Long.parseLong(keyword);
        }catch (NumberFormatException e){
            e.getMessage();
        }

        return !isEmpty(keyword) && memberId != null ? review.member.memberId.eq(memberId) : null;

    }

    private BooleanExpression containStoreId(String keyword) {

        Long storeId = null;

        try {
            storeId = Long.parseLong(keyword);
        }catch (NumberFormatException e){
            e.getMessage();
        }

        return !isEmpty(keyword) && storeId != null? review.store.storeId.eq(storeId) : null;

    }

    private BooleanExpression containContent(String keyword) {

        return !isEmpty(keyword) ? review.content.contains(keyword) : null;

    }

    private BooleanExpression IsOnlyImage(Boolean isOnlyImage) {

        return isOnlyImage != null && isOnlyImage ? review.reviewImageList.isNotEmpty() : null;

    }
}
