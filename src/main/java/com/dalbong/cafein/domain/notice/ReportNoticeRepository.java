package com.dalbong.cafein.domain.notice;

import com.dalbong.cafein.domain.notice.detailReportNotice.DetailReportNotice;
import com.dalbong.cafein.domain.report.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportNoticeRepository extends JpaRepository<ReportNotice, Long> {

    void deleteByReport(Report report);

    @Query("select rpn from ReportNotice rpn " +
            "join fetch rpn.detailReportNotice drpn " +
            "join fetch rpn.report rp " +
            "join fetch rp.reportCategory rpc " +
            "where rpn.noticeId =:noticeId")
    ReportNotice getDetailReportNotice(@Param("noticeId") Long noticeId);
}
