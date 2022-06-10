package com.dalbong.cafein.domain.coupon;

import com.dalbong.cafein.domain.memo.QCouponMemo;
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
import java.util.List;
import java.util.stream.Collectors;

import static com.dalbong.cafein.domain.coupon.QCoupon.coupon;
import static com.dalbong.cafein.domain.memo.QCouponMemo.couponMemo;
import static org.aspectj.util.LangUtil.isEmpty;

public class CouponRepositoryImpl implements CouponRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    public CouponRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 관리자단 쿠폰 리스트 조회
     */
    @Override
    public Page<Object[]> getCouponList(String[] searchType, String keyword, Pageable pageable) {

        JPAQuery<Tuple> query = queryFactory.select(coupon, couponMemo.memoId)
                .from(coupon)
                .leftJoin(coupon.member).fetchJoin()
                .leftJoin(couponMemo).on(couponMemo.coupon.couponId.eq(coupon.couponId))
                .where(searchKeyword(searchType, keyword));

        //정렬
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(Coupon.class, "coupon");
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<Tuple> results = query.fetch();

        //count 쿼리
        JPAQuery<Coupon> countQuery = queryFactory
                .select(coupon)
                .from(coupon)
                .leftJoin(coupon.member).fetchJoin()
                .where(searchKeyword(searchType, keyword));

        List<Object[]> content = results.stream().map(t -> t.toArray()).collect(Collectors.toList());

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());
    }

    /**
     * 관리자단 쿠폰 리스트 사용자 지정 조회
     */
    @Override
    public List<Coupon> getCustomLimitCouponList(int limit) {

        return queryFactory.select(coupon)
                .from(coupon)
                .leftJoin(coupon.member).fetchJoin()
                .orderBy(coupon.couponId.desc())
                .limit(limit)
                .fetch();
    }

    private BooleanBuilder searchKeyword(String[] searchType, String keyword) {

        BooleanBuilder builder = new BooleanBuilder();

        if (searchType != null){ ;
            for(String t : searchType){
                switch (t){
                    case "cp":
                        builder.or(containCouponId(keyword));
                        break;
                    case "m":
                        builder.or(containMemberId(keyword));
                        break;
                    case "p":
                        builder.or(containPhone(keyword));
                        break;
                }
            }
        }
        return builder;
    }

    private BooleanExpression containPhone(String keyword) {

        return !isEmpty(keyword) ? coupon.member.phone.eq(keyword) : null;
    }

    private BooleanExpression containCouponId(String keyword) {

        Long couponId = null;

        try {
            couponId = Long.parseLong(keyword);
        }catch (NumberFormatException e){
            e.getMessage();
        }

        return !isEmpty(keyword) && couponId != null ? coupon.couponId.eq(couponId) : null;


    }

    private BooleanExpression containMemberId(String keyword) {

        Long memberId = null;

        try {
            memberId = Long.parseLong(keyword);
        }catch (NumberFormatException e){
            e.getMessage();
        }

        return !isEmpty(keyword) && memberId != null ? coupon.member.memberId.eq(memberId) : null;

    }


}
