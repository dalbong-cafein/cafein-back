package com.dalbong.cafein.service.review;

import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.dto.admin.review.AdminDetailReviewResDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewListDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewResDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.page.ScrollResultDto;
import com.dalbong.cafein.dto.review.*;
import com.dalbong.cafein.dto.store.RegisteredStoreResDto;
import com.dalbong.cafein.dto.store.StoreListResDto;

import java.io.IOException;
import java.util.List;

public interface ReviewService {

    Review register(ReviewRegDto reviewRegDto, Long principalId) throws IOException;

    void modify(ReviewUpdateDto reviewUpdateDto) throws IOException;

    void remove(Long reviewId);

    ReviewListResDto<ScrollResultDto<ReviewResDto, Object[]>> getReviewListOfStore(PageRequestDto pageRequestDto, Long storeId);


    //StoreListResDto<List<RegisteredStoreResDto>> getRegisteredStoreList(Long principalId);

    DetailReviewScoreResDto getDetailReviewScore(Long storeId);

    AdminReviewListDto getReviewListOfAdmin(PageRequestDto pageRequestDto);

    AdminDetailReviewResDto getDetailReviewOfAdmin(Long reviewId);
}
