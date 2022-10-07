package com.dalbong.cafein.domain.report;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.sticker.ReviewSticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long>, ReportRepositoryQuerydsl {

    @Query("select rp from Report rp " +
            "left join fetch rp.reportCategory " +
            "where rp.toMember.memberId =:memberId")
    List<Report> getReportListByMemberId(@Param("memberId") Long memberId);

    @Query("select count(rp) from Report rp " +
            "where rp.reportStatus = 'APPROVAL' " +
            "and rp.toMember.memberId =:memberId and rp.reportId < :reportId")
    long countApprovalStatusByMemberIdAndLtReportId(@Param("memberId") Long memberId, @Param("reportId") Long reportId);

    List<Report> findByReview(Review review);
}
