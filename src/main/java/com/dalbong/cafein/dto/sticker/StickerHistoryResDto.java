package com.dalbong.cafein.dto.sticker;

import com.dalbong.cafein.domain.notice.BoardNotice;
import com.dalbong.cafein.domain.notice.CouponNotice;
import com.dalbong.cafein.domain.notice.StickerNotice;
import com.dalbong.cafein.domain.sticker.CongestionSticker;
import com.dalbong.cafein.domain.sticker.ReviewSticker;
import com.dalbong.cafein.domain.sticker.Sticker;
import com.dalbong.cafein.domain.sticker.StoreSticker;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StickerHistoryResDto {

    private String stickerType;

    private String storeName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime expDateTime;

    public StickerHistoryResDto(Sticker sticker){

        this.storeName = sticker.getStoreName();
        this.regDateTime = sticker.getRegDateTime();
        this.expDateTime = sticker.getExpDateTime();

        //스티커 알림일 경우
        if(sticker instanceof StoreSticker){
            this.stickerType = "카공 카페 등록";

        }

        //쿠폰 알림일 경우
        if(sticker instanceof ReviewSticker){
            this.stickerType = "카페 리뷰 작성";
        }

        //공지사항 알림일 경우
        if(sticker instanceof CongestionSticker){
            this.stickerType = "혼잡도 공유";
        }
    }
}
