package com.dalbong.cafein.domain.congestion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CongestionQuerydsl {

    /**
     * 혼잡도 3시간이내 등록 여부
     */
    boolean existWithinTime(Long storeId, Long principalId);

    /**
     * 카페별 혼잡도 리스트 조회
     */
    List<Congestion> getCongestionList(Long storeId, Integer minusDays);

}
