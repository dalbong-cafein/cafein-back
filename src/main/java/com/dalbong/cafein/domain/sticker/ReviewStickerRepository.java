package com.dalbong.cafein.domain.sticker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewStickerRepository extends JpaRepository<ReviewSticker, Long> {

    @Query("select rs from ReviewSticker rs where rs.review.reviewId =:reviewId and rs.member.memberId=:memberId")
    Optional<Sticker> findByReviewIdAndMemberId(@Param("reviewId") Long reviewId, @Param("memberId") Long memberId);
}
