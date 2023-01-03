package com.dalbong.cafein.domain.report.report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReportRepositoryQuerydsl {

    boolean existReport(Long reviewId, Long principalId);

    List<Report> findByReportToday();

    Optional<Report> findWithToMemberById(Long reportId);

    Report getLatestApprovalStatusByMemberIdAndNeReportId(Long memberId, Long reportId);

    Page<Object[]> getReportListOfAdmin(String[] searchType, String keyword, Pageable pageable);

    List<Report> getCustomLimitReportListOfAdmin(int limit);
}
