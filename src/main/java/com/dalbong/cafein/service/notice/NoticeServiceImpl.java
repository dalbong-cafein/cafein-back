package com.dalbong.cafein.service.notice;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.notice.*;
import com.dalbong.cafein.domain.report.Report;
import com.dalbong.cafein.domain.sticker.Sticker;
import com.dalbong.cafein.dto.notice.NoticeResDto;
import com.dalbong.cafein.handler.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        switch (reportCnt){
            case 0:
                return reportNoticeRepository.save(new ReportNotice(report, toMember, "신고 1차"));
            case 1:
                return reportNoticeRepository.save(new ReportNotice(report, toMember, "신고 2차"));
            case 2:
                return reportNoticeRepository.save(new ReportNotice(report, toMember, "신고 3차"));
            case 3:
                return reportNoticeRepository.save(new ReportNotice(report, toMember, "신고 4차"));
            default:
                return reportNoticeRepository.save(new ReportNotice(report, toMember, "신고 5차 이상"));
        }
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
     * 알림 삭제
     */
    @Transactional
    @Override
    public void remove(Long noticeId) {

        noticeRepository.deleteById(noticeId);
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
