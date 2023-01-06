package com.dalbong.cafein.domain.notice;

import com.dalbong.cafein.domain.report.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportNoticeRepository extends JpaRepository<ReportNotice, Long> {

    void deleteByReport(Report report);
}
