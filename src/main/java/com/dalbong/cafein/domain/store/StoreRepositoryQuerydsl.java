package com.dalbong.cafein.domain.store;

import com.dalbong.cafein.domain.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreRepositoryQuerydsl {

    /**
     * 앱단 가게 리스트 조회
     */
    List<Object[]> getStoreList(String keyword);

    /**
     * 앱단 내 카페 리스트 조회
     */
    List<Object[]> getMyStoreList(Long principalId);

    /**
     * 앱단 카페 상세 페이지 조회
     */
    Object[] getDetailStore(Long storeId);

    /**
     * 추천 검색 카페 리스트 조회
     */
    List<Store> getRecommendSearchStoreList(String keyword);


    /**
     * 관리자단 전체 가게 리스트 조회
     */
    Page<Object[]> getAllStoreList(String[] searchType, String keyword, Pageable pageable);



}
