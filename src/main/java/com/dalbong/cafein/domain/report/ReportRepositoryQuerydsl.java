package com.dalbong.cafein.domain.report;

import com.dalbong.cafein.domain.member.Member;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepositoryQuerydsl {

    List<Report> findByReportToday();
}
