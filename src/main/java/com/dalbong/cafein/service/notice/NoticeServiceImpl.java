package com.dalbong.cafein.service.notice;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.notice.*;
import com.dalbong.cafein.domain.sticker.Sticker;
import com.dalbong.cafein.dto.notice.NoticeResDto;
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
     * 공지사항 알림 등록
     */
    @Transactional(readOnly = true)
    @Override
    public List<NoticeResDto> getNoticeList(Long principalId) {

        List<Notice> results = noticeRepository.getNoticeList(principalId);

        return results.stream().map(n -> new NoticeResDto(n)).collect(Collectors.toList());
    }
}
