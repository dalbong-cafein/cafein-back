package com.dalbong.cafein.domain.memo;

import com.dalbong.cafein.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewMemoRepository extends JpaRepository<ReviewMemo,Long> {

    void deleteByReview(Review review);
}
