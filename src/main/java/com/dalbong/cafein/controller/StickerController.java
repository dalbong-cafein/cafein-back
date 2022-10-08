package com.dalbong.cafein.controller;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.sticker.StickerHistoryResDto;
import com.dalbong.cafein.dto.sticker.StickerRegDto;
import com.dalbong.cafein.service.sticker.StickerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StickerController {

    private final StickerService stickerService;

    /**
     * 카페 등록시 스티커 발급
     */
    @PostMapping("/stickers/storeType")
    public ResponseEntity<?> issueStoreSticker(@RequestBody StickerRegDto stickerRegDto, @AuthenticationPrincipal PrincipalDetails principalDetails){

        stickerService.issueStoreSticker(stickerRegDto.getStoreId(), principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1,"카페 등록시 스티커 발급 성공", null), HttpStatus.CREATED);
    }

    /**
     * 리뷰 등록시 스티커 발급
     */
    @PostMapping("/stickers/reviewType")
    public ResponseEntity<?> issueReviewSticker(@RequestBody StickerRegDto stickerRegDto, @AuthenticationPrincipal PrincipalDetails principalDetails){

        stickerService.issueReviewSticker(stickerRegDto.getReviewId(), principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1,"리뷰 등록시 스티커 발급 성@공", null), HttpStatus.CREATED);
    }

    /**
     * 혼잡도 스티커 발급 가능 여부
     */
    @GetMapping("/stores/{storeId}/stickers/congestionType/check-possible-issue")
    public ResponseEntity<?> check(@PathVariable("storeId") Long storeId,
                                   @AuthenticationPrincipal PrincipalDetails principalDetails){

        boolean isPossibleIssue = stickerService.checkPossibleIssueCongestionSticker(storeId, principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "혼잡도 스티커 발급 여부 조회 성공", isPossibleIssue), HttpStatus.OK);
    }

    /**
     * 혼잡도 등록시 스티커 발급
     */
    @PostMapping("/stickers/congestionType")
    public ResponseEntity<?> issueCongestionSticker(@RequestBody StickerRegDto stickerRegDto, @AuthenticationPrincipal PrincipalDetails principalDetails){

        stickerService.issueCongestionSticker(stickerRegDto.getCongestionId(), principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1,"혼잡도 등록시 스티커 발급 성공", null), HttpStatus.CREATED);
    }

    /**
     * 회원별 스티커 보유 개수 조회
     */
    @GetMapping("stickers/count-member")
    public ResponseEntity<?> getCountStickerOfMember(@AuthenticationPrincipal PrincipalDetails principalDetails){

        int stickerCnt = stickerService.countStickerOfMember(principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "회원별 스티커 보유 개수 조회 성공", stickerCnt), HttpStatus.OK);
    }

    /**
     * 회원별 스티커 히스토리 내역 조회
     */
    @GetMapping("/members/stickers")
    public ResponseEntity<?> getStickerList(@AuthenticationPrincipal PrincipalDetails principalDetails){

        List<StickerHistoryResDto> stickerHistoryResDtoList = stickerService.getStickerHistoryList(principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "회원별 스티커 히스토리 내역 조회",stickerHistoryResDtoList), HttpStatus.OK);

    }

}
