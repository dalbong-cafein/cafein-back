package com.dalbong.cafein.domain.store;

import com.dalbong.cafein.domain.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreRepositoryQuerydsl {

    /**
     * 전체 가게 리스트 조회
     */
    Page<Object[]> getAllStoreList(String[] searchType, String keyword, Pageable pageable);
}
