package com.dalbong.cafein.domain.review;

import com.dalbong.cafein.domain.image.QMemberImage;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;

import static com.dalbong.cafein.domain.image.QMemberImage.memberImage;
import static com.dalbong.cafein.domain.review.QReview.review;
import static com.querydsl.jpa.JPAExpressions.select;

public class ReviewRepositoryImpl implements ReviewRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    /**
     * 리뷰 리스트 조회
     */
    @Override
    public Page<Object[]> getReviewListOfStore(Long storeId, Pageable pageable) {

        List<Tuple> results = queryFactory
                .select(review, memberImage)
                .from(review)
                .join(review.member).fetchJoin()
                .leftJoin(memberImage).on(memberImage.member.eq(review.member))
                .where(review.store.storeId.eq(storeId))
                .groupBy(review.reviewId)
                .fetch();

        JPAQuery<Tuple> countQuery = queryFactory
                .select(review, memberImage)
                .from(review)
                .join(review.member).fetchJoin()
                .leftJoin(memberImage).on(memberImage.member.memberId.eq(review.member.memberId))
                .groupBy(review.reviewId)
                .where(review.store.storeId.eq(storeId));

        List<Object[]> content = results.stream().map(t -> t.toArray()).collect(Collectors.toList());

        /**
         * 두 경우에는 count 쿼리를 날리지 않음 -> spring Jpa가 지원 (PageableExeutionUtils)
         * 1. 시작 페이지이면서 content 수가 page size보다 작을 경우.
         * 2. 마지막 페이지이면서 content 수가 page size보다 작을 경우.
         * 날릴 필요가 없음.
         */
        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());
    }
}
