package com.dalbong.cafein.domain.report.report;

import com.dalbong.cafein.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long>, ReportRepositoryQuerydsl {

    @Query("select rp, rm.memoId from Report rp " +
            "left join fetch rp.review " +
            "left join fetch rp.toMember " +
            "left join fetch rp.fromMember " +
            "left join fetch rp.reportCategory " +
            "left join ReportMemo rm on rm.report = rp " +
            "where rp.toMember.memberId =:memberId")
    List<Object[]> getReportListOfAdminByMemberId(@Param("memberId") Long memberId);

    @Query("select count(rp) from Report rp " +
            "where rp.reportStatus = 'APPROVAL' " +
            "and rp.toMember.memberId =:memberId and rp.reportId < :reportId")
    long countApprovalStatusByMemberIdAndLtReportId(@Param("memberId") Long memberId, @Param("reportId") Long reportId);

    @Query("select count(rp) from Report rp " +
            "where rp.reportStatus = 'APPROVAL' " +
            "and rp.toMember.memberId =:memberId and rp.reportId <>:reportId")
    long countApprovalStatusByMemberIdAndNeReportId(@Param("memberId") Long memberId, @Param("reportId") Long reportId);

    List<Report> findByReview(Review review);
}
