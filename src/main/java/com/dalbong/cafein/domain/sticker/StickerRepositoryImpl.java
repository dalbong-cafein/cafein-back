package com.dalbong.cafein.domain.sticker;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.dalbong.cafein.domain.sticker.QCongestionSticker.congestionSticker;
import static com.dalbong.cafein.domain.sticker.QReviewSticker.reviewSticker;
import static com.dalbong.cafein.domain.sticker.QSticker.sticker;
import static com.dalbong.cafein.domain.sticker.QStoreSticker.storeSticker;
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
    public boolean existWithinTimeOfCongestionType(Long storeId, Long principalId) {

        Integer fetchOne = queryFactory.selectOne()
                .from(congestionSticker)
                .leftJoin(congestionSticker.congestion)
                .where(congestionSticker.member.memberId.eq(principalId),
                        congestionSticker.congestion.store.storeId.eq(storeId),
                        congestionSticker.regDateTime.between(LocalDateTime.now().minusHours(3),LocalDateTime.now()))
                .fetchFirst();//limit 1

        return fetchOne != null;
    }

    /**
     * 회원별 금일 스티커 수 조회
     */
    @Override
    public long getCountStickerToday(Long principalId) {

        return queryFactory.select(sticker.count())
                .from(sticker)
                .where(sticker.member.memberId.eq(principalId),
                        sticker.regDateTime.between(LocalDateTime.now().toLocalDate().atStartOfDay(),
                                LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59))))
                .fetchCount();
    }

    /**
     * 20개 스티커 조회 - 발급순
     */
    @Override
    public List<Sticker> getCustomLimitStickerList(int limit, Long principalId) {

        return queryFactory.selectFrom(sticker)
                .where(sticker.member.memberId.eq(principalId))
                .orderBy(sticker.stickerId.asc())
                .limit(limit)
                .fetch();
    }

    /**
     * 가장 오래된 카페 스티커 조회
     */
    @Override
    public Optional<Sticker> findByStoreIdAndMemberId(Long storeId, Long memberId) {

        StoreSticker findStoreSticker = queryFactory.select(storeSticker)
                .from(storeSticker)
                .where(storeSticker.store.storeId.eq(storeId), storeSticker.member.memberId.eq(memberId))
                .orderBy(storeSticker.stickerId.asc())
                .limit(1)
                .fetchOne();

        return findStoreSticker != null ? Optional.of(findStoreSticker) : Optional.empty();
    }

    /**
     * 가장 오래된 리뷰 스티커 조회 store fetch join
     */
    @Override
    public Optional<Sticker> findByReviewIdAndMemberId(Long reviewId, Long memberId) {

        ReviewSticker findReviewSticker = queryFactory.select(QReviewSticker.reviewSticker)
                .from(reviewSticker)
                .leftJoin(reviewSticker.review).fetchJoin()
                .leftJoin(reviewSticker.review.store).fetchJoin()
                .where(reviewSticker.review.reviewId.eq(reviewId), reviewSticker.member.memberId.eq(memberId))
                .orderBy(reviewSticker.stickerId.asc())
                .limit(1)
                .fetchOne();

        return findReviewSticker != null ? Optional.of(findReviewSticker) : Optional.empty();
    }

    /**
     * 가장 오래된 혼잡도 스티커 조회 store fetch join
     */
    @Override
    public Optional<Sticker> findByCongestionIdAndMemberId(Long congestionId, Long memberId) {

        CongestionSticker findCongestionSticker = queryFactory.select(congestionSticker)
                .from(congestionSticker)
                .leftJoin(congestionSticker.congestion).fetchJoin()
                .leftJoin(congestionSticker.congestion.store).fetchJoin()
                .where(congestionSticker.congestion.congestionId.eq(congestionId), congestionSticker.member.memberId.eq(memberId))
                .orderBy(congestionSticker.stickerId.asc())
                .limit(1)
                .fetchOne();

        return findCongestionSticker != null ? Optional.of(findCongestionSticker) : Optional.empty();
    }


}
