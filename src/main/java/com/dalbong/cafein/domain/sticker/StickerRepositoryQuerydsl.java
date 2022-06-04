package com.dalbong.cafein.domain.sticker;

import com.dalbong.cafein.domain.congestion.Congestion;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StickerRepositoryQuerydsl {


    /**
     * 3시간 이내 혼잡도 타입 스티커 존재 여부
     */
    boolean existWithinTimeOfCongestionType(Congestion congestion, Long principalId);

    /**
     * 회원별 금일 스티커 수 조회
     */
    long getCountStickerToday(Long principalId);

    /**
     * 20개 스티커 조회 - 발급순
     */
    List<Sticker> getCustomLimitStickerList(int limit, Long principalId);


    /**
     * 가장 오래된 카페 스티커 조회
     */
    Optional<Sticker> findByStoreIdAndMemberId(Long storeId, Long memberId);

    /**
     * 가장 오래된 리뷰 스티커 조회 store fetch join
     */
    Optional<Sticker> findByReviewIdAndMemberId(Long reviewId, Long memberId);

    /**
     * 가장 오래된 혼잡도 스티커 조회 store fetch join
     */
    Optional<Sticker> findByCongestionIdAndMemberId(Long congestionId, Long memberId);
}
