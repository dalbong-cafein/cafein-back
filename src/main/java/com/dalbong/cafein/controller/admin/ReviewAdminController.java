package com.dalbong.cafein.controller.admin;

import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.admin.review.AdminDetailReviewResDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewEvaluationOfStoreResDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewListResDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewResDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class ReviewAdminController {

    private final ReviewService reviewService;

    /**
     * 관리자단 리뷰 삭제
     */
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<?> removeReview(@PathVariable("reviewId") Long reviewId){
        reviewService.remove(reviewId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 리뷰 삭제 성공", null), HttpStatus.OK);
    }

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
}
