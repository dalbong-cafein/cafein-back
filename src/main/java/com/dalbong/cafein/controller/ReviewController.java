package com.dalbong.cafein.controller;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.review.ReviewRegDto;
import com.dalbong.cafein.dto.review.ReviewUpdateDto;
import com.dalbong.cafein.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

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
