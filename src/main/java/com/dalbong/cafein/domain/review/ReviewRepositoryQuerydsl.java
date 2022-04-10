package com.dalbong.cafein.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryQuerydsl {

    /**
     * 리뷰 리스트 조회
     */
    Page<Object[]> getReviewListOfStore(Long storeId, Pageable pageable);
}
