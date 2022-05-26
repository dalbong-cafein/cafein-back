package com.dalbong.cafein.controller;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.page.ScrollResultDto;
import com.dalbong.cafein.dto.review.*;
import com.dalbong.cafein.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 리스트 조회
     */
    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<?> getReviewListOfStore(@PathVariable("storeId") Long storeId, PageRequestDto requestDto){

        ReviewListResDto<ScrollResultDto<ReviewResDto, Object[]>> reviewListResDto = reviewService.getReviewListOfStore(requestDto, storeId);

        return new ResponseEntity<>(new CMRespDto<>(1, "리뷰 리스트 조회 성공", reviewListResDto), HttpStatus.OK);
    }

    /**
     * 카페 상세 화면 - 리뷰 리스트 개수지정 조회
     */
    @GetMapping("/stores/{storeId}/reviews/limit")
    public ResponseEntity<?> getCustomLimitReviewListOfStore(@PathVariable("storeId") Long storeId,
                                                             @RequestParam(value = "limit",defaultValue = "3", required = false) int limit){

        ReviewListResDto<List<ReviewResDto>> reviewListResDto = reviewService.getCustomLimitReviewListOfStore(limit, storeId);

        return new ResponseEntity<>(new CMRespDto<>(1, "리뷰 리스트 개수지정 조회 성공", reviewListResDto), HttpStatus.OK);
    }

    /**
     * 카페별 상세 리뷰 점수 조회
     */
    @GetMapping("/stores/{storeId}/detail-review-score")
    public ResponseEntity<?> getDetailReviewScore(@PathVariable("storeId") Long storeId){

        DetailReviewScoreResDto detailReviewScoreResDto = reviewService.getDetailReviewScore(storeId);

        return new ResponseEntity<>(new CMRespDto<>(1, "상세 리뷰 점수 조회 성공", detailReviewScoreResDto), HttpStatus.OK);
    }

    /**
     * 회원별 리뷰 리스트 조회
     */
    @GetMapping("/members/reviews")
    public ResponseEntity<?> getMyReviewList(@AuthenticationPrincipal PrincipalDetails principalDetails){

        ReviewListResDto<List<MyReviewResDto>> reviewListResDto = reviewService.getMyReviewList(principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "내가 쓴 리뷰 리스트 조회 성공", reviewListResDto),HttpStatus.OK);
    }

    /**
     * 리뷰 등록
     */
    @PostMapping("/reviews")
    public ResponseEntity<?> register(@Validated ReviewRegDto reviewRegDto, BindingResult bindingResult,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        reviewService.register(reviewRegDto, principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1,"리뷰 등록 성공",null), HttpStatus.CREATED);
    }

    /**
     * 리뷰 수정
     */
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<?> modify(@Validated ReviewUpdateDto reviewUpdateDto, BindingResult bindingResult) throws IOException {

        reviewService.modify(reviewUpdateDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "리뷰 수정 성공", null), HttpStatus.OK);
    }


    /**
     * 리뷰 삭제
     */
    @DeleteMapping("reviews/{reviewId}")
    public ResponseEntity<?> remove(@PathVariable("reviewId") Long reviewId){

        reviewService.remove(reviewId);

        return new ResponseEntity<>(new CMRespDto<>(1, "리뷰 삭제 성공",null), HttpStatus.OK);
    }
}
