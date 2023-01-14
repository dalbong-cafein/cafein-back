package com.dalbong.cafein.domain.notice.detailReportNotice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetailReportNoticeRepository extends JpaRepository<DetailReportNotice, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from DetailReportNotice drpn " +
            "where drpn in (:detailReportNoticeList)")
    void deleteInBatch(@Param("detailReportNoticeList") List<DetailReportNotice> detailReportNoticeList);

}
