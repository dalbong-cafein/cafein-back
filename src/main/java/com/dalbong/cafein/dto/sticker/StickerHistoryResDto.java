package com.dalbong.cafein.dto.sticker;

import com.dalbong.cafein.domain.notice.BoardNotice;
import com.dalbong.cafein.domain.notice.CouponNotice;
import com.dalbong.cafein.domain.notice.StickerNotice;
import com.dalbong.cafein.domain.sticker.*;
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

        //카페 스티커일 경우
        if(sticker instanceof StoreSticker){
            this.stickerType = "카공 카페 등록";
        }

        //리뷰 스티커일 경우
       if(sticker instanceof ReviewSticker){
            this.stickerType = "카페 리뷰 작성";
        }

        //혼잡도 스티커일 경우
        if(sticker instanceof CongestionSticker){
            this.stickerType = "혼잡도 공유";
        }

        //이벤트 혜택 스티커일 경우
        if(sticker instanceof EventSticker){
            this.stickerType = "리뷰 이벤트 혜택";
        }
    }
}
