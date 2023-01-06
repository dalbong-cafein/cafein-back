package com.dalbong.cafein.domain.notice.detailReportNotice;

import com.dalbong.cafein.domain.notice.ReportNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DetailReportNoticeRepository extends JpaRepository<DetailReportNotice, Long> {

    @Query("select drpn from DetailReportNotice drpn " +
            "join fetch drpn.reportNotice rpn " +
            "join fetch rpn.report rp " +
            "join fetch rp.reportCategory rpc " +
            "where rpn.noticeId =:noticeId")
    DetailReportNotice getDetailReportNotice(@Param("noticeId") Long noticeId);
}
