package com.dalbong.cafein.controller.admin;

import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.reportCategory.ReportCategoryResDto;
import com.dalbong.cafein.service.reportCategory.ReportCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class ReportCategoryAdminController {

    private final ReportCategoryService reportCategoryService;

    /**
     * 관리자단 신고사유 리스트 조회
     */
    @GetMapping("/reportCategorys")
    public ResponseEntity<?> getReportCategoryList(){

        List<ReportCategoryResDto> reportCategoryResDtoList = reportCategoryService.getReportCategoryList();

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 신고사유 카테고리 리스트 조회 성공", reportCategoryResDtoList), HttpStatus.OK);
    }

}
