package com.dalbong.cafein.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ReviewRepositoryQuerydsl {

    /**
     * 가게별 리뷰 리스트 조회
     */
    Page<Object[]> getReviewListOfStore(Long storeId, Boolean isOnlyImage, Pageable pageable);

    /**
     * 가게별 리뷰 리스트 개수 지정 조회
     */
    List<Object[]> getCustomLimitReviewList(int limit, Long storeId);

    /**
     * 관리자단 전체 리뷰 리스트 조회
     */
    Page<Review> getAllReviewList(String[] searchType, String keyword, Pageable pageable);

    /**
     * 회원별 리뷰 리스트 조회
     */
    List<Object[]> getMyReviewList(Long principalId);

    /**
     * 관리자단 리뷰 상세 정보 조회
     */
    Optional<Object[]> getDetailReview(Long reviewId);
}
