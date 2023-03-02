package com.dalbong.cafein.dto.notice;

import com.dalbong.cafein.domain.notice.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NoticeResDto {

    private Long noticeId;

    private String noticeType;

    private String content;

    private Boolean isRead;

    private Long stickerId;

    private Long couponId;

    private Long boardId;

    private Long reportId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    public NoticeResDto(Notice notice){

        this.noticeId = notice.getNoticeId();
        this.content = notice.getContent();
        this.isRead = notice.getIsRead();
        this.regDateTime = notice.getRegDateTime();

        //스티커 알림일 경우
        if(notice instanceof StickerNotice){
            this.noticeType = "스티커";
            this.stickerId = ((StickerNotice) notice).getSticker().getStickerId();
        }

        //쿠폰 알림일 경우
        if(notice instanceof CouponNotice){
            this.noticeType = "쿠폰";
            this.couponId = ((CouponNotice) notice).getCoupon().getCouponId();
        }

        //공지사항 알림일 경우
        if(notice instanceof BoardNotice){
            this.noticeType = "공지사항";
            this.boardId = ((BoardNotice) notice).getBoard().getBoardId();
        }

        //신고 알림일 경우
        if(notice instanceof ReportNotice){
            this.noticeType = "신고";
            this.reportId = ((ReportNotice) notice).getReport().getReportId();
        }



    }

}
