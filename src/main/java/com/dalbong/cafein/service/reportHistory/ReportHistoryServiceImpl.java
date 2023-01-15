package com.dalbong.cafein.service.reportHistory;

import com.dalbong.cafein.domain.report.ReportStatus;
import com.dalbong.cafein.domain.report.report.Report;
import com.dalbong.cafein.domain.report.reportHistory.ReportHistory;
import com.dalbong.cafein.domain.report.reportHistory.ReportHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ReportHistoryServiceImpl implements ReportHistoryService{

    private final ReportHistoryRepository reportHistoryRepository;

    /**
     * 신고 상태 히스토리 생성
     */
    @Override
    public ReportHistory save(Report report, ReportStatus status) {

        ReportHistory reportHistory = ReportHistory.builder()
                .report(report)
                .reportStatus(status)
                .build();

        return reportHistoryRepository.save(reportHistory);
    }
}
