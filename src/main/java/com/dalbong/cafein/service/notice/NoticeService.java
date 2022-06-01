package com.dalbong.cafein.service.notice;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.notice.Notice;
import com.dalbong.cafein.domain.sticker.Sticker;
import com.dalbong.cafein.dto.notice.NoticeResDto;

import java.util.List;

public interface NoticeService {

    Notice registerStickerNotice(Sticker sticker, Member toMember);

    Notice registerCouponNotice(Coupon coupon, Member toMember);

    void registerBoardNotice(Board board, List<Member> toMemberList);

    void read(Long noticeId);

    List<NoticeResDto> getNoticeList(Long principalId);


}
