package com.dalbong.cafein.service.notice;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.notice.*;
import com.dalbong.cafein.domain.sticker.Sticker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService{

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
    public Notice registerBoardNotice(Board board ,Member toMember) {

        BoardNotice boardNotice = new BoardNotice(board, toMember);

        return boardNoticeRepository.save(boardNotice);
    }
}
