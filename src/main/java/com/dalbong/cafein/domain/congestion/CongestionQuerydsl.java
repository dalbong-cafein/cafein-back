package com.dalbong.cafein.domain.congestion;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CongestionQuerydsl {

    /**
     * 혼잡도 2시간이내 등록 여부
     */
    boolean existWithinTime(Long storeId, Long principalId);

}
