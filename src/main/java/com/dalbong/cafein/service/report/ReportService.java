package com.dalbong.cafein.service.report;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.report.Report;
import com.dalbong.cafein.dto.admin.report.AdminReportListResDto;
import com.dalbong.cafein.dto.report.ReportRegDto;

public interface ReportService {

    Report report(ReportRegDto reportRegDto, Member fromMember);

    void autoModifyMemberState();

    void autoModifyToSuspension();

    void autoModifyToNormal();

    AdminReportListResDto getReportListOfAdmin(Long memberId);
}
