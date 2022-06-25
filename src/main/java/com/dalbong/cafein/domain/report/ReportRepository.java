package com.dalbong.cafein.domain.report;

import com.dalbong.cafein.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long>, ReportRepositoryQuerydsl {

    @Query("select rp from Report rp " +
            "left join fetch rp.reportCategory " +
            "left join Review r on r.reviewId = rp.review.reviewId " +
            "where r.member.memberId =:memberId")
    List<Report> getReportListByMemberId(@Param("memberId") Long memberId);

    @Query("select count(rp) from Report rp left join Review r on r.reviewId = rp.review.reviewId " +
            " where rp.review.member.memberId =: memberId")
    long countByMemberId(@Param("memberId") Long memberId);

    @Query("select m from Report rp " +
            "left join Review r on r.reviewId = rp.review.reviewId " +
            "left join Member m on m.memberId = r.member.memberId " +
            "where rp.regDateTime.between(LocalDateTime.now().toLocalDate().atStartOfDay(), LocalDateTime.of(LocalDate.now().LocalTime.of(23, 59, 59)))")
    List<Member> findMemberByReportToday();

    @Query("select m from Report rp " +
            "left join Review r on r.reviewId = rp.review.reviewId " +
            "left join Member m on m.memberId = r.member.memberId " +
            "where rp.regDateTime between now. ")
    List<Member> findMemberByReportExpiredToday();



}
