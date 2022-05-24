package com.dalbong.cafein.domain.sticker;

import com.dalbong.cafein.domain.congestion.Congestion;

public interface StickerRepositoryQuerydsl {


    /**
     * 3시간 이내 혼잡도 타입 스티커 존재 여부
     */
    boolean existWithinTimeOfCongestionType(Congestion congestion, Long principalId);
}