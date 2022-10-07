package com.dalbong.cafein.domain.report;

import com.dalbong.cafein.domain.member.Member;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReportRepositoryQuerydsl {

    List<Report> findByReportToday();

    Optional<Report> findWithToMemberById(Long reportId);
}
