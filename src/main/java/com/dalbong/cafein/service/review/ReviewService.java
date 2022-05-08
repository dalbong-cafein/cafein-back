package com.dalbong.cafein.service.review;

import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.dto.admin.review.AdminReviewListDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.review.*;

import java.io.IOException;

public interface ReviewService {

    Review register(ReviewRegDto reviewRegDto, Long principalId) throws IOException;

    void modify(ReviewUpdateDto reviewUpdateDto) throws IOException;

    void remove(Long reviewId);

    ReviewListResDto getReviewListOfStore(PageRequestDto pageRequestDto, Long storeId);

    DetailReviewScoreResDto getDetailReviewScore(Long storeId);

    AdminReviewListDto getReviewListOfAdmin(PageRequestDto pageRequestDto);
}
