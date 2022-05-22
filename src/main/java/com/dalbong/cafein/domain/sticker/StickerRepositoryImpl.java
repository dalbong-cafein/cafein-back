package com.dalbong.cafein.domain.sticker;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

import static com.dalbong.cafein.domain.sticker.QCongestionSticker.congestionSticker;
import static com.dalbong.cafein.domain.sticker.QSticker.sticker;

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
}
