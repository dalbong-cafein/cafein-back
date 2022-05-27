package com.dalbong.cafein.service.report;

import com.dalbong.cafein.domain.report.Report;
import com.dalbong.cafein.domain.report.ReportRepository;
import com.dalbong.cafein.dto.admin.report.AdminReportListResDto;
import com.dalbong.cafein.dto.admin.report.AdminReportResDto;
import com.dalbong.cafein.dto.report.ReportRegDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService{

    private final ReportRepository reportRepository;

    /**
     * 신고하기
     */
    @Transactional
    @Override
    public Report report(ReportRegDto reportRegDto) {

        Report report = reportRegDto.toEntity();

        return reportRepository.save(report);
    }

    /**
     * 관리자단 회원별 신고 내역 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminReportListResDto getReportListOfAdmin(Long memberId) {

        List<Report> reportList = reportRepository.getReportListByMemberId(memberId);

        List<AdminReportResDto> adminReportResDtoList =
                reportList.stream().map(report -> new AdminReportResDto(report)).collect(Collectors.toList());

        return new AdminReportListResDto(reportList.size(), adminReportResDtoList);
    }
}
