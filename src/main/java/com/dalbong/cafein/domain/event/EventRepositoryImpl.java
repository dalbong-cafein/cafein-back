package com.dalbong.cafein.domain.event;

import com.dalbong.cafein.domain.image.EventImage;
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

import static com.dalbong.cafein.domain.event.QEvent.event;
import static com.dalbong.cafein.domain.image.QEventImage.eventImage;

public class EventRepositoryImpl implements EventRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    public EventRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * 이벤트 배너 리스트 조회
     */
    @Override
    public Page<Object[]> getEventList(Pageable pageable) {

        JPAQuery<Tuple> query = queryFactory.select(event, eventImage)
                .from(event)
                .leftJoin(eventImage).on(eventImage.event.eventId.eq(event.eventId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        //정렬
        for (Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(Event.class, "event");
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty())));
        }

        List<Tuple> results = query.fetch();

        //count 쿼리
        JPAQuery<Event> countQuery = queryFactory.select(event)
                .from(event)
                .leftJoin(eventImage).on(eventImage.event.eventId.eq(event.eventId));

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
