package com.dalbong.cafein.service.report;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.domain.report.ReportStatus;
import com.dalbong.cafein.domain.report.report.Report;
import com.dalbong.cafein.domain.report.report.ReportRepository;
import com.dalbong.cafein.domain.report.reportHistory.ReportHistory;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.review.ReviewRepository;
import com.dalbong.cafein.dto.PossibleRegistrationResDto;
import com.dalbong.cafein.dto.admin.report.AdminReportListResDto;
import com.dalbong.cafein.dto.admin.report.AdminReportResDto;
import com.dalbong.cafein.dto.admin.reportHistory.AdminReportHistoryResDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.page.PageResultDTO;
import com.dalbong.cafein.dto.report.ReportRegDto;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.service.notice.NoticeService;
import com.dalbong.cafein.service.reportHistory.ReportHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService{

    private final ReportRepository reportRepository;
    private final ReviewRepository reviewRepository;
    private final NoticeService noticeService;
    private final MemberRepository memberRepository;
    private final ReportHistoryService reportHistoryService;

    /**
     * 신고 가능 여부 체크
     */
    @Transactional(readOnly = true)
    @Override
    public PossibleRegistrationResDto checkPossibleReport(Long reviewId, Long principalId) {

        if(reportRepository.existReport(reviewId, principalId)){
            return new PossibleRegistrationResDto(false, "이미 신고된 리뷰입니다.");
        }

        return new PossibleRegistrationResDto(true, null);
    }

    /**
     * 신고하기
     */
    @Transactional
    @Override
    public Report report(ReportRegDto reportRegDto, Member fromMember) {

        Review review = reviewRepository.findById(reportRegDto.getReviewId()).orElseThrow(() ->
                new CustomException("존재하지 않는 리뷰입니다."));

        if(reportRepository.existReport(reportRegDto.getReviewId(), fromMember.getMemberId())){
            throw new CustomException("이미 신고된 리뷰입니다.");
        }

        Report report = reportRegDto.toEntity(review, fromMember);

        reportRepository.save(report);

        //신고 상태 히스토리 생성 - 대기
        reportHistoryService.save(report, ReportStatus.WAIT);

        return report;
    }

    /**
     * 신고 승인하기
     */
    @Transactional
    @Override
    public void approve(Long reportId) {

        //Report 조회 (fetch toMember)
        Report report = reportRepository.findWithToMemberById(reportId).orElseThrow(() ->
                new CustomException("존재하지 않는 신고입니다."));

        report.approve();

        //리뷰 정책 적용
        reportPolicy(report, report.getToMember());

        //신고 상태 히스토리 생성 - 승인
        reportHistoryService.save(report, ReportStatus.APPROVAL);

        //해당 리뷰 게시중단
        Review review = reviewRepository.findById(report.getReview().getReviewId()).orElseThrow(() ->
                new CustomException("존재하지 않는 리뷰입니다."));
        review.stopPosting();
    }

    /**
     * 신고 반려하기
     */
    @Transactional
    @Override
    public void reject(Long reportId) {

        Report report = reportRepository.findById(reportId).orElseThrow(() ->
                new CustomException("존재하지 않는 신고입니다."));

        //승인 -> 반려
        if(report.getReportStatus().equals(ReportStatus.APPROVAL)){

            Member member = memberRepository.findById(report.getToMember().getMemberId()).orElseThrow(() ->
                    new CustomException("존재하지 않는 회원입니다."));
            //패널티 철회
            withdrawPenalty(report, member);
        }

        report.reject();

        //신고 상태 히스토리 생성 - 반려
        reportHistoryService.save(report, ReportStatus.REJECT);
    }

    private void withdrawPenalty(Report report, Member member) {

        int reportCnt = (int) reportRepository.countApprovalStatusByMemberIdAndNeReportId(member.getMemberId(), report.getReportId());

        Optional<Report> optLatestApprovalReport = reportRepository.getLatestApprovalStatusByMemberIdAndNeReportId(member.getMemberId(), report.getReportId());

        LocalDateTime reportExpiredDateTime = getReportExpiredDate(reportCnt, optLatestApprovalReport);

        //활동 정지 유효기간이 지났을 경우
        if (reportExpiredDateTime == null || !LocalDate.now().isBefore(reportExpiredDateTime.toLocalDate())) {
            member.changeToNormal();
        }
        //활동 정지 유효기간이 지나지 않았을 경우
        else {
            member.changeReportExpiredDateTime(reportExpiredDateTime);
        }
    }

    private LocalDateTime getReportExpiredDate(int reportCnt, Optional<Report> optLatestApprovalReport) {

        if(optLatestApprovalReport.isPresent()){

            LocalDateTime reportExpiredDateTime = optLatestApprovalReport.get().getModDateTime();

            switch (reportCnt) {
                case 0:
                case 1:
                    reportExpiredDateTime = null;
                    break;
                case 2:
                    reportExpiredDateTime = reportExpiredDateTime.plusDays(1);
                    break;
                case 3:
                    reportExpiredDateTime = reportExpiredDateTime.plusDays(3);
                    break;
                case 4:
                    reportExpiredDateTime = reportExpiredDateTime.plusDays(7);
                    break;
                default:
                    reportExpiredDateTime = reportExpiredDateTime.plusMonths(1);
            }
            return reportExpiredDateTime;

        }else{
                return null;
            }
    }

    /**
     * 자정에 정지 회원 변동 기능 실행
     */
    @Scheduled(cron = "00 00 00 * * ?")
    @Transactional
    @Override
    public void autoModifyMemberState() {

        autoModifyToNormal();
        //autoModifyToSuspension();
    }


    /**
     * 금일 신고 확인 후 회원 정지 상태로 변경
     */
    @Transactional
    @Override
    public void autoModifyToSuspension() {

        List<Report> reportList = reportRepository.findByReportToday();

        reportList.forEach(report -> {

            Member toMember = report.getToMember();

            reportPolicy(report, toMember);
        });
    }

    private void reportPolicy(Report report, Member toMember) {

        int reportCnt = (int) reportRepository.countApprovalStatusByMemberIdAndLtReportId(toMember.getMemberId(), report.getReportId());

        //회원정지
        if (reportCnt > 0){
            toMember.suspend(reportCnt);
        }

        //회원정지 알림 생성
        noticeService.registerReportNotice(report, toMember, reportCnt);
    }

    /**
     * 회원 정지기간 확인 후 일반 회원 상태로 변경
     */
    @Transactional
    @Override
    public void autoModifyToNormal() {

        List<Member> results = memberRepository.findByReportExpiredToday();

        results.forEach(Member::changeToNormal);
    }

    /**
     * 관리자단 회원별 신고 내역 조회
     */
    @Transactional(readOnly = true)
    @Override
    public List<AdminReportResDto> getReportListOfAdminByMemberId(Long memberId) {

        List<Object[]> reportList = reportRepository.getReportListOfAdminByMemberId(memberId);

        return reportList.stream().map(arr -> {

            Report report = (Report) arr[0];

            //신고 상태 히스토리 리스트
            List<AdminReportHistoryResDto> reportHistoryResDtoList = getReportHistoryList(report);

            return new AdminReportResDto((Report) arr[0], reportHistoryResDtoList, (Long) arr[1]);
        }).collect(Collectors.toList());
    }


    /**
     * 관리자단 신고 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminReportListResDto<?> getReportListOfAdmin(PageRequestDto pageRequestDto) {

        Pageable pageable;

        if(pageRequestDto.getSort().equals("ASC")){
            pageable = pageRequestDto.getPageable(Sort.by("reportId").ascending());
        }else{
            pageable = pageRequestDto.getPageable(Sort.by("reportId").descending());
        }

        Page<Object[]> results = reportRepository.getReportListOfAdmin(pageRequestDto.getSearchType(), pageRequestDto.getKeyword(), pageable);

        Function<Object[], AdminReportResDto> fn = (arr -> {

            Report report = (Report) arr[0];

            //신고 상태 히스토리 리스트
            List<AdminReportHistoryResDto> reportHistoryResDtoList = getReportHistoryList(report);

            return new AdminReportResDto(report, reportHistoryResDtoList, (Long) arr[1]);
        });

        return new AdminReportListResDto<>(results.getTotalElements(), new PageResultDTO<>(results, fn));
    }

    /**
     * 관리자단 신고 리스트 사용자 개수 지정 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminReportListResDto<?> getCustomLimitReportListOfAdmin(int limit) {

        List<Report> reportList = reportRepository.getCustomLimitReportListOfAdmin(limit);

        List<AdminReportResDto> adminReportResDtoList = reportList.stream().map(report ->
                new AdminReportResDto(report, getReportHistoryList(report))).collect(Collectors.toList());

        return new AdminReportListResDto<>(reportList.size(), adminReportResDtoList);
    }

    private List<AdminReportHistoryResDto> getReportHistoryList(Report report) {

        List<ReportHistory> reportHistoryList = report.getReportHistoryList();

        List<AdminReportHistoryResDto> reportHistoryResDtoList = new ArrayList<>();

        if(reportHistoryList != null && !reportHistoryList.isEmpty()){
            for(ReportHistory reportHistory : reportHistoryList){
                reportHistoryResDtoList.add(new AdminReportHistoryResDto(reportHistory));
            }
        }
        return reportHistoryResDtoList;
    }
}
