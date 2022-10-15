package com.dalbong.cafein.controller;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.image.ImageDto;
import com.dalbong.cafein.dto.member.*;
import com.dalbong.cafein.service.member.MemberService;
import com.dalbong.cafein.service.review.ReviewService;
import com.dalbong.cafein.service.store.StoreService;
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
public class MemberController {

    private final MemberService memberService;
    private final StoreService storeService;
    private final ReviewService reviewService;

    /**
     * 휴대폰 번호 수정
     */
    @PatchMapping("/members/agree-terms")
    public ResponseEntity<?> saveAgreeTerms(@Validated @RequestBody AgreeRegDto agreeRegDto, BindingResult bindingResult,
                                            @AuthenticationPrincipal PrincipalDetails principalDetails){

        memberService.saveAgreeTerms(agreeRegDto, principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "약관동의 데이터 저장 성공", null), HttpStatus.OK);
    }

    /**
     * 휴대폰 번호 수정
     */
    @PatchMapping("/members/{memberId}/phone")
    public ResponseEntity<?> modifyPhone(@Validated @RequestBody PhoneUpdateDto phoneUpdateDto, BindingResult bindingResult,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails){

        memberService.modifyPhone(phoneUpdateDto.getPhone(), principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "휴대폰 번호 수정 성공",null), HttpStatus.OK);
    }

    /**
     * 프로필 사진 및 닉네임 수정
     */
    @PatchMapping("/members/{memberId}/ImageAndNickname")
    public ResponseEntity<?> modifyImageAndNinckanme(@Validated MemberUpdateDto memberUpdateDto, BindingResult bindingResult,
                                                     @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        ImageDto imageDto = memberService.modifyImageAndNickname(memberUpdateDto, principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "프로필 사진 및 닉네임 수정 성공", imageDto), HttpStatus.OK);
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping("/members")
    public ResponseEntity<?> leave(@AuthenticationPrincipal PrincipalDetails principalDetails){

        memberService.leave(principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "회원탈퇴 성공", null), HttpStatus.OK);
    }

    /**
     * 회원 정보 조회
     */
    @GetMapping("/members/info")
    public ResponseEntity<?> getMemberInfo(@AuthenticationPrincipal PrincipalDetails principalDetails){

        MemberInfoDto memberInfoDto = memberService.getMemberInfo(principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "회원 정보 조회 성공", memberInfoDto), HttpStatus.OK);
    }

    /**
     * 본인이 등록한 카페, 리뷰 개수 조회
     */
    @GetMapping("/members/storesAndReviews/count")
    public ResponseEntity<?> getCountStoresAndReview(@AuthenticationPrincipal PrincipalDetails principalDetails){

        int storeCnt = storeService.countMyRegisterStore(principalDetails.getMember().getMemberId());

        int reviewCnt = reviewService.countMyReview(principalDetails.getMember().getMemberId());

        StoreAndReviewCntResDto storeAndReviewCntResDto = new StoreAndReviewCntResDto(storeCnt, reviewCnt);

        return new ResponseEntity<>(new CMRespDto<>(1, "내가 등록한 카페, 리뷰 개수 조회 성공", storeAndReviewCntResDto), HttpStatus.OK);

    }
}
