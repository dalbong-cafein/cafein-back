package com.dalbong.cafein.service.notice;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.notice.*;
import com.dalbong.cafein.domain.notice.detailReportNotice.DetailReportNotice;
import com.dalbong.cafein.domain.report.report.Report;
import com.dalbong.cafein.domain.sticker.Sticker;
import com.dalbong.cafein.dto.notice.NoticeResDto;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.service.notice.detailReportNotice.DetailReportNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService{

    private final NoticeRepository noticeRepository;
    private final StickerNoticeRepository stickerNoticeRepository;
    private final CouponNoticeRepository couponNoticeRepository;
    private final BoardNoticeRepository boardNoticeRepository;
    private final ReportNoticeRepository reportNoticeRepository;
    private final DetailReportNoticeService detailReportNoticeService;

    /**
     * 스티커 지급 알림 등록
     */
    @Transactional
    @Override
    public Notice registerStickerNotice(Sticker sticker, Member toMember) {

        StickerNotice stickerNotice = new StickerNotice(sticker, toMember);

        return stickerNoticeRepository.save(stickerNotice);
    }

    /**
     * 쿠폰 지급 알림 등록
     */
    @Transactional
    @Override
    public Notice registerCouponNotice(Coupon coupon, Member toMember) {

        CouponNotice couponNotice = new CouponNotice(coupon, toMember);

        return couponNoticeRepository.save(couponNotice);
    }

    /**
     * 신고 알림 등록
     */
    @Transactional
    @Override
    public Notice registerReportNotice(Report report, Member toMember, int reportCnt) {

        //활동 제한 기한
        LocalDateTime reportExpiredDateTime = toMember.getReportExpiredDateTime();
        int year = reportExpiredDateTime.getYear();
        int month = reportExpiredDateTime.getMonthValue();
        int day = reportExpiredDateTime.getDayOfMonth()-1;

        //신고 텍스트
        String reportText = "작성한 리뷰에 대해 신고가 접수되어 카페인 활동이 제한되었습니다. " +
                "신고 정책에 따라 하루 동안 카페 리뷰 작성, 혼잡도 공유 활동이 제한되며 " +
                "신고된 리뷰는 게시중단 처리됩니다."+System.lineSeparator()+
                "*활동 제한 기한: ~ "+year+"년 "+month+"월 "+day+"일까지";

        Notice notice;
        switch (reportCnt){
            case 0:
                notice = reportNoticeRepository.save(new ReportNotice(report, toMember,
                        "[신고 1회] 작성한 리뷰에 대해 신고가 접수되어 안내드립니다."));
                break;
            case 1:
                notice = reportNoticeRepository.save(new ReportNotice(report, toMember,
                        "[신고 2회] " + reportText));
                break;
            case 2:
                notice = reportNoticeRepository.save(new ReportNotice(report, toMember,
                        "[신고 3회] "+ reportText));
                break;
            case 3:
                notice = reportNoticeRepository.save(new ReportNotice(report, toMember,
                        "[신고 4회] "+ reportText));
                break;
            default:
                reportCnt += 1;
                notice = reportNoticeRepository.save(new ReportNotice(report, toMember,
                        "[신고 "+ reportCnt +"회] " + reportText));
        }

        //상세 신고 알림 저장
        detailReportNoticeService.register(notice, toMember.getReportExpiredDateTime(), LocalDateTime.now());
        return notice;
    }

    /**
     * 공지사항 알림 등록
     */
    @Transactional
    @Override
    public void registerBoardNotice(Board board , List<Member> toMemberList) {

        List<BoardNotice> boardNoticeList =
                toMemberList.stream().map(m -> new BoardNotice(board, m))
                        .collect(Collectors.toList());

        boardNoticeRepository.saveAll(boardNoticeList);
    }

    /**
     * 알림 읽음 처리
     */
    @Transactional
    @Override
    public void read(Long noticeId) {

        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() ->
                new CustomException("존재하지 않는 알림입니다."));

        notice.read();
    }

    /**
     * 알림 삭제 - by noticeId
     */
    @Transactional
    @Override
    public void remove(Long noticeId) {

        noticeRepository.deleteById(noticeId);
    }

    /**
     * 알림 삭제 - By report
     */
    @Transactional
    @Override
    public void remove(Report report) {

        reportNoticeRepository.deleteByReport(report);
    }

    /**
     * 회원별 전체 알림 삭제
     */
    @Transactional
    @Override
    public void removeAll(Long principalId) {

        noticeRepository.deleteByMemberId(principalId);
    }

    /**
     * 알림 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public List<NoticeResDto> getNoticeList(Long principalId) {

        List<Notice> results = noticeRepository.getNoticeList(principalId);

        return results.stream().map(n -> new NoticeResDto(n)).collect(Collectors.toList());
    }
}
