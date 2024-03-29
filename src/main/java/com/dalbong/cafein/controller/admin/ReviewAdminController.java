package com.dalbong.cafein.controller.admin;

import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.admin.review.*;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class ReviewAdminController {

    private final ReviewService reviewService;

    /**
     * 관리자단 리뷰 리스트 조회
     */
    @GetMapping("/reviews")
    public ResponseEntity<?> getAllReviewList(PageRequestDto requestDto){

        AdminReviewListResDto adminReviewListResDto = reviewService.getReviewListOfAdmin(requestDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 리뷰 리스트 조회 성공", adminReviewListResDto), HttpStatus.OK);
    }

    /**
     * 관리자단 리뷰 상세 정보 조회
     */
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<?> getDetailReview(@PathVariable("reviewId") Long reviewId){

        AdminDetailReviewResDto adminDetailReviewResDto= reviewService.getDetailReviewOfAdmin(reviewId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 리뷰 상세 정보 조회 성공", adminDetailReviewResDto), HttpStatus.OK);
    }

    /**
     * 관리자단 카페별 상세 리뷰 점수 조회
     */
    @GetMapping("/stores/{storeId}/reviews/detail-evaluation")
    public ResponseEntity<?> getReviewDetailEvaluationOfStore(@PathVariable("storeId") Long storeId){

        AdminReviewEvaluationOfStoreResDto adminReviewEvaluationOfStoreResDto =
                reviewService.getReviewDetailEvaluationOfStore(storeId);

        return new ResponseEntity<>(new CMRespDto<>(
                1, "관리자단 카페 리뷰 상세 평가 정보 조회 성공", adminReviewEvaluationOfStoreResDto), HttpStatus.OK);
    }

    /**
     * 관리자단 회원별 리뷰 리스트 조회
     */
    @GetMapping("/members/{memberId}/reviews")
    public ResponseEntity<?> getReviewListOfMember(@PathVariable("memberId") Long memberId){

        List<AdminReviewResDto> adminReviewResDtoList = reviewService.getReviewListByMemberIdOfAdmin(memberId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 회원별 리뷰 리스트 조회 성공", adminReviewResDtoList), HttpStatus.OK);
    }

    /**
     * 관리자단 리뷰 수정
     */
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<?> modify(@Validated AdminReviewUpdateDto adminReviewUpdateDto, BindingResult bindingResult) throws IOException {

        reviewService.modifyOfAdmin(adminReviewUpdateDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 리뷰 수정 성공", null), HttpStatus.OK);
    }

    /**
     * 리뷰 게시 상태로 변경
     */
    @PatchMapping("/reviews/{reviewId}/post")
    public ResponseEntity<?> post(@PathVariable("reviewId") Long reviewId){

        reviewService.post(reviewId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 리뷰 게시 상태로 변경 성공", null), HttpStatus.OK);
    }

    /**
     * 리뷰 게시중단 상태로 변경
     */
    @PatchMapping("/reviews/{reviewId}/stop-posting")
    public ResponseEntity<?> stopPosting(@PathVariable("reviewId") Long reviewId){

        reviewService.stopPosting(reviewId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 리뷰 게시중단 상태로 변경 성공", null), HttpStatus.OK);
    }

    /**
     * 관리자단 리뷰 삭제
     */
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<?> removeReview(@PathVariable("reviewId") Long reviewId){

        reviewService.remove(reviewId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 리뷰 삭제 성공", null), HttpStatus.OK);
    }
}
