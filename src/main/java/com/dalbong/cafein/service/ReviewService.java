package com.dalbong.cafein.service;

import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.dto.review.ReviewRegDto;

public interface ReviewService {

    Review register(ReviewRegDto reviewRegDto, Long principalId);
}
