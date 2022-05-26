package com.dalbong.cafein.service.report;

import com.dalbong.cafein.domain.report.Report;
import com.dalbong.cafein.domain.report.ReportRepository;
import com.dalbong.cafein.dto.report.ReportRegDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService{

    private final ReportRepository reportRepository;

    /**
     * 신고하기
     */
    @Transactional
    @Override
    public Report report(ReportRegDto reportRegDto) {

        Report report = reportRegDto.toEntity();

        return reportRepository.save(report);
    }
}
