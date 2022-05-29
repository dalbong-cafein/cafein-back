package com.dalbong.cafein.domain.sticker;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.dalbong.cafein.domain.sticker.QCongestionSticker.congestionSticker;
import static com.dalbong.cafein.domain.sticker.QSticker.sticker;
import static com.dalbong.cafein.domain.store.QStore.store;

public class StickerRepositoryImpl implements StickerRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    public StickerRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    /**
     * 3시간 이내 혼잡도 타입 스티커 존재 여부
     */
    @Override
    public boolean existWithinTimeOfCongestionType(Congestion congestion, Long principalId) {

        Integer fetchOne = queryFactory.selectOne()
                .from(congestionSticker)
                .leftJoin(congestionSticker.congestion)
                .where(congestionSticker.member.memberId.eq(principalId),
                        congestionSticker.congestion.store.storeId.eq(congestion.getStore().getStoreId()),
                        congestionSticker.regDateTime.between(LocalDateTime.now().minusHours(3),LocalDateTime.now()))
                .fetchFirst();//limit 1

        return fetchOne != null;
    }

    /**
     * 회원별 금일 스티커 수 조회
     */
    @Override
    public long getCountStickerToday(Long principalId) {

        System.out.println(LocalDateTime.now().toLocalDate().atStartOfDay().toString());

        return queryFactory.select(sticker.count())
                .from(sticker)
                .where(sticker.member.memberId.eq(principalId),
                        sticker.regDateTime.between(LocalDateTime.now().toLocalDate().atStartOfDay(),
                                LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 59, 59))))
                .fetchCount();
    }


}
