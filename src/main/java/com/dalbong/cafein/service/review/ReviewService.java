package com.dalbong.cafein.service.review;

import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.page.ScrollResultDto;
import com.dalbong.cafein.dto.review.ReviewListDto;
import com.dalbong.cafein.dto.review.ReviewRegDto;
import com.dalbong.cafein.dto.review.ReviewResDto;
import com.dalbong.cafein.dto.review.ReviewUpdateDto;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface ReviewService {

    Review register(ReviewRegDto reviewRegDto, Long principalId) throws IOException;

    void modify(ReviewUpdateDto reviewUpdateDto) throws IOException;

    void remove(Long reviewId);

    ReviewListDto getReviewListOfStore(PageRequestDto pageRequestDto, Long storeId);
}
