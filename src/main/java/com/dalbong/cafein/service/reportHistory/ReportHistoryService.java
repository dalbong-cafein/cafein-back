package com.dalbong.cafein.service.reportHistory;

import com.dalbong.cafein.domain.report.ReportStatus;
import com.dalbong.cafein.domain.report.report.Report;
import com.dalbong.cafein.domain.report.reportHistory.ReportHistory;

public interface ReportHistoryService {

    ReportHistory save(Report report, ReportStatus status);

}
