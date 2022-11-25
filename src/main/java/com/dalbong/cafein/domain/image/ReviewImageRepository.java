package com.dalbong.cafein.domain.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage,Long> {

    @Query("select ri from ReviewImage ri where ri.review.reviewId = :reviewId")
    List<ReviewImage> findReviewImagesByReviewReviewId(@Param("reviewId") Long reviewId);

    @Query("select ri from ReviewImage ri " +
            "join fetch ri.review r " +
            "join fetch r.member " +
            "where ri.review.store.storeId = :storeId")
    List<ReviewImage> findWithRegMemberByStoreId(@Param("storeId") Long storeId);
}
