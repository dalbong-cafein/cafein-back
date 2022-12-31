package com.dalbong.cafein.domain.notice;

import com.dalbong.cafein.domain.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportNoticeRepository extends JpaRepository<ReportNotice, Long> {

    void deleteByReport(Report report);
}
