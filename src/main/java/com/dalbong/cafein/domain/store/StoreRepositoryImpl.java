package com.dalbong.cafein.domain.store;

import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.congestion.QCongestion;
import com.dalbong.cafein.domain.review.Review;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.tomcat.jni.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.dalbong.cafein.domain.congestion.QCongestion.congestion;
import static com.dalbong.cafein.domain.review.QReview.review;
import static com.dalbong.cafein.domain.store.QStore.store;
import static org.aspectj.util.LangUtil.isEmpty;

public class StoreRepositoryImpl implements  StoreRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    public StoreRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 전체 가게 리스트 조회
     */
    @Override
    public Page<Object[]> getAllStoreList(String[] searchType, String keyword, Pageable pageable) {

        JPAQuery<Tuple> query = queryFactory
                .select(store, store.reviewList.size(), congestion.congestionScore.avg())
                .from(store)
                .leftJoin(congestion).on(congestion.store.storeId.eq(store.storeId))
                .where(searchKeyword(searchType, keyword),
                        congestion.regDateTime.between(LocalDateTime.now().minusHours(1), LocalDateTime.now())
                                .or(congestion.regDateTime.isNull()))
                .groupBy(store.storeId);

        //정렬
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(Store.class, "store");
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<Tuple> results = query.fetch();

        //count 쿼리
        JPAQuery<Tuple> countQuery = queryFactory
                .select(store, store.reviewList.size(), congestion.congestionScore.avg())
                .from(store)
                .leftJoin(congestion).on(congestion.store.storeId.eq(store.storeId))
                .where(searchKeyword(searchType, keyword),
                        congestion.regDateTime.between(LocalDateTime.now().minusHours(1), LocalDateTime.now()))
                .groupBy(store.storeId);

        List<Object[]> content = results.stream().map(t -> t.toArray()).collect(Collectors.toList());

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetchCount());

    }

    private BooleanBuilder searchKeyword(String[] searchType, String keyword) {

        BooleanBuilder builder = new BooleanBuilder();

        if (searchType != null){
            for(String t : searchType){
                switch (t){
                    case "s":
                        builder.or(containStoreId(keyword));
                        break;
                    case "sn":
                        builder.or(containStoreName(keyword));
                        break;
                    case "a":
                        builder.or(containAddress(keyword));
                }
            }
        }
        return builder;
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

    private BooleanExpression containStoreName(String keyword) {

        return !isEmpty(keyword) ? store.storeName.contains(keyword) : null;

    }

    private BooleanExpression containAddress(String keyword) {

        return !isEmpty(keyword) ? store.address.sggNm.contains(keyword): null;

    }
}
