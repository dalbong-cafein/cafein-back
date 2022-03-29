package com.dalbong.cafein.service.review;

import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.dto.review.ReviewRegDto;
import com.dalbong.cafein.dto.review.ReviewUpdateDto;

import java.io.IOException;

public interface ReviewService {

    Review register(ReviewRegDto reviewRegDto, Long principalId) throws IOException;

    void modify(ReviewUpdateDto reviewUpdateDto) throws IOException;

    void remove(Long reviewId);
}
