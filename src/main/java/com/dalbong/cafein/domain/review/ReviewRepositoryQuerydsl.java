package com.dalbong.cafein.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReviewRepositoryQuerydsl {

    /**
     * 가게별 리뷰 리스트 조회
     */
    Page<Object[]> getReviewListOfStore(Long storeId, Boolean isOnlyImage, Pageable pageable);

    /**
     * 전체 리뷰 리스트 조회
     */
    Page<Review> getAllReviewList(String[] searchType, String keyword, Pageable pageable);

    /**
     * 관리자단 리뷰 상세 정보 조회
     */
    Optional<Object[]> getDetailReview(Long reviewId);
}
