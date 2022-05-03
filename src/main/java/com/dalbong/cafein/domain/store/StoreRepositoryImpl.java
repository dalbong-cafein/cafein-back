package com.dalbong.cafein.domain.store;

import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.board.QBoard;
import com.dalbong.cafein.domain.businessHours.QBusinessHours;
import com.dalbong.cafein.domain.congestion.Congestion;
import com.dalbong.cafein.domain.congestion.QCongestion;
import com.dalbong.cafein.domain.heart.QHeart;
import com.dalbong.cafein.domain.image.QStoreImage;
import com.dalbong.cafein.domain.review.Review;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringExpressions;
import com.querydsl.jpa.JPAExpressions;
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

import static com.dalbong.cafein.domain.businessHours.QBusinessHours.businessHours;
import static com.dalbong.cafein.domain.congestion.QCongestion.congestion;
import static com.dalbong.cafein.domain.heart.QHeart.heart;
import static com.dalbong.cafein.domain.image.QStoreImage.storeImage;
import static com.dalbong.cafein.domain.review.QReview.review;
import static com.dalbong.cafein.domain.store.QStore.store;
import static org.aspectj.util.LangUtil.isEmpty;

public class StoreRepositoryImpl implements  StoreRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    public StoreRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 앱단 가게 리스트 조회
     */
    @Override
    public List<Object[]> getStoreList(String keyword) {

        QCongestion subCongestion = new QCongestion("sub");

        //TODO 위치 검색 추가 필요
        List<Tuple> result = queryFactory
                .select(store, store.heartList.size(), JPAExpressions
                        .select(subCongestion.congestionScore.avg())
                        .from(subCongestion)
                        .where(subCongestion.regDateTime.between(LocalDateTime.now().minusHours(1), LocalDateTime.now()),
                                subCongestion.store.storeId.eq(store.storeId)))
                .from(store)
                .join(store.businessHours).fetchJoin()
                .leftJoin(storeImage).on(storeImage.store.storeId.eq(store.storeId))
                .where(containStoreName(keyword))
                .groupBy(store.storeId)
                .fetch();

        return result.stream().map(t -> t.toArray()).collect(Collectors.toList());
    }
    /**
     *
     * 앱단 내 카페 리스트 조회
     */
    @Override
    public List<Object[]> getMyStoreList(Long principalId) {

        QCongestion subCongestion = new QCongestion("sub");

        List<Tuple> result = queryFactory
                .select(store, JPAExpressions
                        .select(subCongestion.congestionScore.avg())
                        .from(subCongestion)
                        .where(subCongestion.regDateTime.between(LocalDateTime.now().minusHours(1), LocalDateTime.now()),
                                subCongestion.store.storeId.eq(store.storeId)))
                .from(store)
                .join(store.businessHours).fetchJoin()
                .leftJoin(storeImage).on(storeImage.store.storeId.eq(store.storeId))
                .join(heart).on(heart.store.storeId.eq(store.storeId))
                .where(heart.member.memberId.eq(principalId))
                .groupBy(store.storeId)
                .fetch();

        return result.stream().map(t -> t.toArray()).collect(Collectors.toList());
    }

    /**
     * 관리자단 가게 리스트 조회
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

        return !isEmpty(keyword) ? Expressions.asBoolean(store.address.toString().contains(keyword)).isTrue(): null;

    }
}
