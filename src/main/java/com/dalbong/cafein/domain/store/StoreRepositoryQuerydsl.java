package com.dalbong.cafein.domain.store;

import com.dalbong.cafein.domain.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreRepositoryQuerydsl {

    /**
     * 가게 리스트 조회
     */
    List<Object[]> getStoreList(String keyword);


    /**
     * 전체 가게 리스트 조회
     */
    Page<Object[]> getAllStoreList(String[] searchType, String keyword, Pageable pageable);

    /**
     * 추천 검색 카페 리스트 조회
     */
    List<Store> getRecommendSearchStoreList(String keyword);
}
