package com.dalbong.cafein.domain.store;

import com.dalbong.cafein.domain.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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
     * 앱단 본인이 등록한 가게 리스트 조회
     */
    List<Store> getMyRegisterStoreList(Long principalId);

    /**
     * 엡단 본인이 등록한 가게 리스트 개수 지정 조회
     */
    List<Store> getCustomLimitReviewList(int limit, Long principalId);

    /**
     * 앱단 카페 상세 페이지 조회
     */
    Optional<Object[]> getDetailStore(Long storeId);

    /**
     * 추천 검색 카페 리스트 조회
     */
    List<Store> getRecommendSearchStoreList(String keyword);


    /**
     * 관리자단 전체 카페 리스트 조회
     */
    Page<Object[]> getAllStoreList(String[] searchType, String keyword, Pageable pageable);

    /**
     * 관리자단 카페 상세 페이지 조회
     */
    Optional<Object[]> getDetailStoreOfAdmin(Long storeId);

}
