package com.dalbong.cafein.service.report;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.domain.member.MemberState;
import com.dalbong.cafein.domain.report.Report;
import com.dalbong.cafein.domain.report.ReportRepository;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.review.ReviewRepository;
import com.dalbong.cafein.dto.admin.report.AdminReportListResDto;
import com.dalbong.cafein.dto.admin.report.AdminReportResDto;
import com.dalbong.cafein.dto.report.ReportRegDto;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService{

    private final ReportRepository reportRepository;
    private final ReviewRepository reviewRepository;
    private final NoticeService noticeService;
    private final MemberRepository memberRepository;

    /**
     * 신고하기
     */
    @Transactional
    @Override
    public Report report(ReportRegDto reportRegDto) {

        Review review = reviewRepository.findById(reportRegDto.getReviewId()).orElseThrow(() ->
                new CustomException("존재하지 않는 리뷰입니다."));

        Member member = memberRepository.findById(review.getMember().getMemberId()).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));

        long reportCnt = reportRepository.countByMemberId(review.getMember().getMemberId());

        Report report = reportRegDto.toEntity(review);

        reportRepository.save(report);

        reportPolicy(report, member, (int)reportCnt);

        return report;
    }

    private void reportPolicy(Report report, Member member, int reportCnt) {

        //회원정지
        if (reportCnt > 0){
            member.suspension(reportCnt);
        }

        //정지알림 생성
        noticeService.registerReportNotice(report, member, reportCnt);
    }

    /**
     * 관리자단 회원별 신고 내역 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminReportListResDto getReportListOfAdmin(Long memberId) {

        List<Report> reportList = reportRepository.getReportListByMemberId(memberId);

        List<AdminReportResDto> adminReportResDtoList =
                reportList.stream().map(report -> new AdminReportResDto(report)).collect(Collectors.toList());

        return new AdminReportListResDto(reportList.size(), adminReportResDtoList);
    }
}
