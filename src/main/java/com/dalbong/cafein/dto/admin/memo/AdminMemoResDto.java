package com.dalbong.cafein.dto.admin.memo;

import com.dalbong.cafein.domain.memo.*;
import com.dalbong.cafein.domain.notice.BoardNotice;
import com.dalbong.cafein.domain.notice.CouponNotice;
import com.dalbong.cafein.domain.notice.StickerNotice;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminMemoResDto {

    private Long memoId;

    private String memoType;

    private Long storeId;

    private Long reviewId;

    private Long memberId;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime modDateTime;

    public AdminMemoResDto(Memo memo){

        this.memoId = memo.getMemoId();
        this.content = memo.getContent();
        this.regDateTime = memo.getRegDateTime();
        this.modDateTime = memo.getModDateTime();

        //카페 메모일 경우
        if(memo instanceof StoreMemo){
            this.memoType = "카페관리";
            this.storeId = ((StoreMemo) memo).getStore().getStoreId();
        }

        //리뷰 메모일 경우
        if(memo instanceof ReviewMemo){
            this.memoType = "리뷰관리";
            this.reviewId = ((ReviewMemo) memo).getReview().getReviewId();
        }

        //회원 메모일 경우
        if(memo instanceof MemberMemo){
            this.memoType = "회원관리";
            this.memberId = ((MemberMemo) memo).getMember().getMemberId();
        }

        //쿠폰 메모일 경우
        if(memo instanceof CouponMemo){
            this.memoType = "쿠폰관리";
            this.memberId = ((CouponMemo) memo).getCoupon().getCouponId();
        }

    }

}
