package com.dalbong.cafein.controller;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.report.ReportRegDto;
import com.dalbong.cafein.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ReportController {

    private final ReportService reportService;

    /**
     * 리뷰 신고하기
     */
    @PostMapping("/reviews/{reviewId}/reports")
    public ResponseEntity<?> report(@Validated @RequestBody ReportRegDto reportRegDto, BindingResult bindingResult,
                                    @AuthenticationPrincipal PrincipalDetails principalDetails){

        reportService.report(reportRegDto, principalDetails.getMember());

        return new ResponseEntity<>(new CMRespDto<>(1, "리뷰 신고하기 성공",null), HttpStatus.CREATED);
    }

}
