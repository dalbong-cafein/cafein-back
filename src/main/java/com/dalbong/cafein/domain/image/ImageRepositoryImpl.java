package com.dalbong.cafein.domain.image;

import com.dalbong.cafein.domain.review.Review;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
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

import static com.dalbong.cafein.domain.image.QEventImage.eventImage;
import static com.dalbong.cafein.domain.image.QMemberImage.memberImage;

public class ImageRepositoryImpl implements ImageRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    public ImageRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 이벤트 이미지 리스트 조회
     */
    @Override
    public Page<EventImage> getEventImageList(Pageable pageable) {

        JPAQuery<EventImage> query = queryFactory.select(eventImage)
                .from(eventImage)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        //정렬
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(EventImage.class, "imageId");
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<EventImage> results = query.fetch();

        //count 쿼리
        JPAQuery<EventImage> countQuery = queryFactory.select(eventImage)
                .from(eventImage);


        /**
         * 두 경우에는 count 쿼리를 날리지 않음 -> spring Jpa가 지원 (PageableExeutionUtils)
         * 1. 시작 페이지이면서 content 수가 page size보다 작을 경우.
         * 2. 마지막 페이지이면서 content 수가 page size보다 작을 경우.
         * 날릴 필요가 없음.
         */
        return PageableExecutionUtils.getPage(results, pageable, () -> countQuery.fetchCount());
    }
}
