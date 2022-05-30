package com.dalbong.cafein.service.report;

import com.dalbong.cafein.domain.report.Report;
import com.dalbong.cafein.dto.admin.report.AdminReportListResDto;
import com.dalbong.cafein.dto.report.ReportRegDto;

public interface ReportService {

    Report report(ReportRegDto reportRegDto);

    AdminReportListResDto getReportListOfAdmin(Long memberId);
}