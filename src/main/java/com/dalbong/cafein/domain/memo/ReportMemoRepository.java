package com.dalbong.cafein.domain.memo;

import com.dalbong.cafein.domain.report.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportMemoRepository extends JpaRepository<ReportMemo,Long> {

    void deleteByReport(Report report);

}
