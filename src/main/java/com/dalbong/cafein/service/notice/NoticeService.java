package com.dalbong.cafein.service.notice;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.notice.Notice;
import com.dalbong.cafein.domain.sticker.Sticker;

public interface NoticeService {

    Notice registerStickerNotice(Sticker sticker, Member toMember);

    Notice registerCouponNotice(Coupon coupon, Member toMember);

    Notice registerBoardNotice(Board board, Member toMember);


}
