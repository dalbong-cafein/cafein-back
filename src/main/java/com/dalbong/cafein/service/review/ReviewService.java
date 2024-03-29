package com.dalbong.cafein.service.review;

import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.dto.PossibleRegistrationResDto;
import com.dalbong.cafein.dto.admin.review.*;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.page.ScrollResultDto;
import com.dalbong.cafein.dto.review.*;
import java.io.IOException;
import java.util.List;

public interface ReviewService {

    PossibleRegistrationResDto checkPossibleRegistration(Long storeId, Long principalId);

    Review register(ReviewRegDto reviewRegDto, Long principalId) throws IOException;

    void modify(ReviewUpdateDto reviewUpdateDto) throws IOException;

    void post(Long reviewId);

    void stopPosting(Long reviewId);

    void remove(Long reviewId);

    ReviewListResDto<ScrollResultDto<ReviewResDto, Object[]>> getReviewListOfStore(PageRequestDto pageRequestDto, Long storeId);

    ReviewListResDto<List<MyReviewResDto>> getMyReviewList(Long principalId);

    ReviewListResDto<List<ReviewResDto>> getCustomLimitReviewListOfStore(int limit, Long storeId);

    DetailReviewScoreResDto getDetailReviewScore(Long storeId);

    void modifyOfAdmin(AdminReviewUpdateDto adminReviewUpdateDto) throws IOException;

    AdminReviewListResDto getReviewListOfAdmin(PageRequestDto pageRequestDto);

    AdminDetailReviewResDto getDetailReviewOfAdmin(Long reviewId);

    AdminReviewEvaluationOfStoreResDto getReviewDetailEvaluationOfStore(Long storeId);

    List<AdminReviewResDto> getReviewListByMemberIdOfAdmin(Long memberId);

    Long getRegisterCountOfToday();

    int countMyReview(Long memberId);
}
