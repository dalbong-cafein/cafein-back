package com.dalbong.cafein.controller;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewListDto;
import com.dalbong.cafein.dto.admin.store.AdminStoreListDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.review.ReviewListDto;
import com.dalbong.cafein.dto.store.StoreRegDto;
import com.dalbong.cafein.service.review.ReviewService;
import com.dalbong.cafein.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final ReviewService reviewService;
    private final StoreService storeService;

    /**
     * 전체 리뷰 리스트 조회
     */
    @GetMapping("/reviews")
    public ResponseEntity<?> getAllReviewList(PageRequestDto requestDto){

        AdminReviewListDto adminReviewListDto = reviewService.getReviewListOfAdmin(requestDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 리뷰 리스트 조회 성공", adminReviewListDto), HttpStatus.OK);
    }

    /**
     * 관리자단 가게 등록
     */
    @PostMapping("/stores")
    public ResponseEntity<?> register(@Validated StoreRegDto storeRegDto,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        storeService.register(storeRegDto,principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1,"관리자단 가게 등록 성공",null), HttpStatus.CREATED);
    }

    /**
     * 관리자단 가게 등록
     */
    @GetMapping("/stores")
    public ResponseEntity<?> getAllStoreList(PageRequestDto requestDto){

        AdminStoreListDto adminStoreListDto = storeService.getStoreListOfAdmin(requestDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 카페 리스트 조회 성공", adminStoreListDto), HttpStatus.OK);
    }
}
