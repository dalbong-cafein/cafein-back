package com.dalbong.cafein.service.notice;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.notice.Notice;
import com.dalbong.cafein.domain.report.report.Report;
import com.dalbong.cafein.domain.sticker.Sticker;
import com.dalbong.cafein.dto.notice.NoticeResDto;

import java.util.List;

public interface NoticeService {

    Notice registerStickerNotice(Sticker sticker, Member toMember);

    Notice registerCouponNotice(Coupon coupon, Member toMember);

    Notice registerReportNotice(Report report, Member toMember, int reportCnt);

    void registerBoardNotice(Board board, List<Member> toMemberList);

    void read(Long noticeId);

    void remove(Long noticeId);

    void removeAll(Long principalId);

    List<NoticeResDto> getNoticeList(Long principalId);


}
