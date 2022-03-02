package com.dalbong.cafein.service.review;

import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.dto.review.ReviewRegDto;

public interface ReviewService {

    Review register(ReviewRegDto reviewRegDto, Long principalId);
}
