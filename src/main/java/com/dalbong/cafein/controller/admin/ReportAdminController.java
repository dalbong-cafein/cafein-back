package com.dalbong.cafein.controller.admin;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.PossibleRegistrationResDto;
import com.dalbong.cafein.dto.admin.report.AdminReportListResDto;
import com.dalbong.cafein.dto.admin.report.AdminReportResDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.report.ReportRegDto;
import com.dalbong.cafein.dto.reportCategory.ReportCategoryResDto;
import com.dalbong.cafein.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class ReportAdminController {

    private final ReportService reportService;

    /**
     * 관리자단 신고 가능 여부 조회
     */
    @GetMapping("/reviews/{reviewId}/reports/check-possible-report")
    public ResponseEntity<?> checkPossibleReport(@PathVariable("reviewId") Long reviewId,
                                                 @AuthenticationPrincipal PrincipalDetails principalDetails){

        PossibleRegistrationResDto possibleRegistrationResDto = reportService.checkPossibleReport(reviewId, principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 신고 가능 여부 조회 성공", possibleRegistrationResDto), HttpStatus.OK);
    }


    /**
     * 관리자단 리뷰 신고하기
     */
    @PostMapping("/reviews/{reviewId}/reports")
    public ResponseEntity<?> report(@Validated @RequestBody ReportRegDto reportRegDto, BindingResult bindingResult,
                                    @AuthenticationPrincipal PrincipalDetails principalDetails){

        //reportService.report(reportRegDto, principalDetails.getMember());
        reportService.report(reportRegDto, Member.builder().memberId(1L).build());

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 리뷰 신고하기 성공",null), HttpStatus.CREATED);
    }

    /**
     * 관리자단 리뷰 신고 승인하기
     */
    @PatchMapping("/reports/{reportId}/approve")
    public ResponseEntity<?> approveReport(@PathVariable("reportId") Long reportId){

        reportService.approve(reportId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 리뷰 신고 승인하기 성공", null), HttpStatus.OK);
    }

    /**
     * 관리자단 리뷰 신고 반려하기
     */
    @PatchMapping("/reports/{reportId}/reject")
    public ResponseEntity<?> rejectReport(@PathVariable("reportId") Long reportId){

        reportService.reject(reportId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 리뷰 신고 반려하기 성공", null), HttpStatus.OK);
    }

    /**
     * 관리지단 신고 리스트 조회
     */
    @GetMapping("/reports")
    public ResponseEntity<?> getReportList(PageRequestDto requestDto){

        AdminReportListResDto<?> adminReportListResDto = reportService.getReportListOfAdmin(requestDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 신고 리스트 조회 성공", adminReportListResDto), HttpStatus.OK);
    }

    /**
     * 관리자단 신고 리스트 개수 지정 조회
     */
    @GetMapping("/reports/limit")
    public ResponseEntity<?> getCustomLimitReportList(@RequestParam(required = false, defaultValue = "6") int limit){

        AdminReportListResDto<?> adminReportListResDto = reportService.getCustomLimitReportListOfAdmin(limit);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 신고 리스트 개수 지정 조회 성공", adminReportListResDto), HttpStatus.OK);
    }

    /**
     * 관리자단 회원별 신고내역 조회
     */
    @GetMapping("/members/{memberId}/reports")
    public ResponseEntity<?> getReportListOfMember(@PathVariable("memberId") Long memberId){

        List<AdminReportResDto> adminReportResDtoList = reportService.getReportListOfAdminByMemberId(memberId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 회원별 신고내역 조회 성공", adminReportResDtoList), HttpStatus.OK);
    }
}
