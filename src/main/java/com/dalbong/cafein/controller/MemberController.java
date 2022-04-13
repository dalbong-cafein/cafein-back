package com.dalbong.cafein.controller;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.member.MemberInfoDto;
import com.dalbong.cafein.dto.member.MemberUpdateDto;
import com.dalbong.cafein.dto.member.PhoneUpdateDto;
import com.dalbong.cafein.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    /**
     * 휴대폰 번호 수정
     */
    @PatchMapping("/members/{memberId}/phone")
    public ResponseEntity<?> modifyPhone(@RequestBody PhoneUpdateDto phoneUpdateDto,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails){

        memberService.modifyPhone(phoneUpdateDto.getPhone(), principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "휴대폰 번호 수정 성공",null), HttpStatus.OK);
    }

    /**
     * 프로필 사진 및 닉네임 수정
     */
    @PatchMapping("/members/{memberId}/ImageAndNickname")
    public ResponseEntity<?> modifyImageAndNinckanme(MemberUpdateDto memberUpdateDto,
                                                     @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        memberService.modifyImageAndNickname(memberUpdateDto, principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "프로필 사진 및 닉네임 수정 성공",null), HttpStatus.OK);
    }

    /**
     * 회원 정보 조회
     */
    @GetMapping("/members/info")
    public ResponseEntity<?> getMemberInfo(@AuthenticationPrincipal PrincipalDetails principalDetails){

        MemberInfoDto memberInfoDto = memberService.getMemberInfo(principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "회원 정보 조회 성공", memberInfoDto), HttpStatus.OK);
    }
}
