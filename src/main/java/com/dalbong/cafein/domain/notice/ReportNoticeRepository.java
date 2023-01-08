package com.dalbong.cafein.domain.notice;

import com.dalbong.cafein.domain.notice.detailReportNotice.DetailReportNotice;
import com.dalbong.cafein.domain.report.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReportNoticeRepository extends JpaRepository<ReportNotice, Long> {

    void deleteByReport(Report report);

    @Query("select rpn from ReportNotice rpn " +
            "join fetch rpn.detailReportNotice drpn " +
            "join fetch rpn.report rp " +
            "join fetch rp.reportCategory rpc " +
            "where rpn.noticeId =:noticeId")
    Optional<ReportNotice> getDetailReportNotice(@Param("noticeId") Long noticeId);

    @Query("select drpn from ReportNotice rpn " +
            "join DetailReportNotice drpn on drpn.detailReportNoticeId = rpn.detailReportNotice.detailReportNoticeId " +
            "where rpn.toMember.memberId = :memberId")
    List<DetailReportNotice> getDetailReportNoticeByMemberId(@Param("memberId") Long memberId);
}
