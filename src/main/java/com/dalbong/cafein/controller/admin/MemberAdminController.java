package com.dalbong.cafein.controller.admin;

import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.admin.member.AdminDetailMemberResDto;
import com.dalbong.cafein.dto.admin.member.AdminMemberListResDto;
import com.dalbong.cafein.dto.admin.member.AdminMemberUpdateDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class MemberAdminController {

    private final MemberService memberService;

    /**
     * 관리자단 회원 정보 수정 (생년월일, 성별, 휴대폰 번호)
     */
    @PutMapping("/members")
    public ResponseEntity<?> modifyMemberInfo(@Validated @RequestBody AdminMemberUpdateDto adminMemberUpdateDto,
                                              BindingResult bindingResult){

        memberService.modifyOfAdmin(adminMemberUpdateDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 회원 정보 수정 성공",null), HttpStatus.OK);
    }

    /**
     * 관리자단 회원 탈퇴 기능
     */
    @DeleteMapping("members/{memberId}")
    public ResponseEntity<?> leave(@PathVariable("memberId") Long memberId){
        memberService.leave(memberId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 회원 탈퇴 성공", null), HttpStatus.OK);
    }

    /**
     * 관리자단 회원 리스트 조회
     */
    @GetMapping("/members")
    public ResponseEntity<?> getMemberList(PageRequestDto requestDto){

        AdminMemberListResDto adminMemberListResDto = memberService.getMemberListOfAdmin(requestDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 회원 리스트 조회 성공", adminMemberListResDto), HttpStatus.OK);
    }

    /**
     * 관리자단 회원 상세 조회
     */
    @GetMapping("/members/{memberId}")
    public ResponseEntity<?> getDetailMember(@PathVariable("memberId") Long memberId){

        AdminDetailMemberResDto adminDetailMemberResDto = memberService.getDetailMemberOfAdmin(memberId);

        return new ResponseEntity<>(
                new CMRespDto<>(1, "관리자단 상세 회원 조회 성공", adminDetailMemberResDto), HttpStatus.OK);
    }
}
