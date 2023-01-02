package com.dalbong.cafein.service.report;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.domain.member.MemberState;
import com.dalbong.cafein.domain.memo.ReportMemo;
import com.dalbong.cafein.domain.notice.NoticeRepository;
import com.dalbong.cafein.domain.report.Report;
import com.dalbong.cafein.domain.report.ReportRepository;
import com.dalbong.cafein.domain.report.ReportStatus;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.review.ReviewRepository;
import com.dalbong.cafein.dto.PossibleRegistrationResDto;
import com.dalbong.cafein.dto.admin.report.AdminReportListResDto;
import com.dalbong.cafein.dto.admin.report.AdminReportResDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewListResDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewResDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.page.PageResultDTO;
import com.dalbong.cafein.dto.report.ReportRegDto;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

            //noticeService.remove(report);
            
            Member member = memberRepository.findById(report.getToMember().getMemberId()).orElseThrow(() ->
                    new CustomException("존재하지 않는 회원입니다."));

            member.changeToNormal();
        }

        report.reject();
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

        return reportList.stream().map(arr -> new AdminReportResDto((Report) arr[0], (Long) arr[1]))
                        .collect(Collectors.toList());
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

            return new AdminReportResDto(report, (Long) arr[1]);
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
                new AdminReportResDto(report)).collect(Collectors.toList());

        return new AdminReportListResDto<>(reportList.size(), adminReportResDtoList);
    }
}
