package com.dalbong.cafein.service.report;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.report.Report;
import com.dalbong.cafein.dto.admin.member.AdminMemberListResDto;
import com.dalbong.cafein.dto.admin.report.AdminReportListResDto;
import com.dalbong.cafein.dto.admin.report.AdminReportResDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.report.ReportRegDto;

import java.util.List;

public interface ReportService {

    Report report(ReportRegDto reportRegDto, Member fromMember);

    void approve(Long reportId);

    void reject(Long reportId);

    void autoModifyMemberState();

    void autoModifyToSuspension();

    void autoModifyToNormal();

    List<AdminReportResDto> getReportListOfAdminByMemberId(Long memberId);

    AdminReportListResDto getReportListOfAdmin(PageRequestDto pageRequestDto);
}
