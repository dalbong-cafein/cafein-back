package com.dalbong.cafein.domain.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Query("select rp from Report rp " +
            "left join fetch rp.reportCategory " +
            "left join Review r on r.reviewId = rp.review.reviewId " +
            "where r.member.memberId =:memberId")
    List<Report> getReportListByMemberId(@Param("memberId") Long memberId);

    @Query("select count(rp) from Report rp left join Review r on r.reviewId = rp.review.reviewId " +
            " where rp.review.member.memberId =: memberId")
    long countByMemberId(@Param("memberId") Long memberId);



}
