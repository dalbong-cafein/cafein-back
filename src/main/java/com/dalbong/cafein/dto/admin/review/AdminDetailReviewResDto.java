package com.dalbong.cafein.dto.admin.review;

import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.dto.image.ImageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminDetailReviewResDto {

    private Long reviewId;

    private Long writerId;

    private String nicknameOfWriter;

    private long visitCnt;

    private Long storeId;

    private String storeName;

    private String content;

    private List<ImageDto> reviewImageDtoList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime modDateTime;

    public AdminDetailReviewResDto(Review review, long visitCnt, List<ImageDto> reviewImageDtoList){
        this.reviewId = review.getReviewId();
        this.writerId = review.getMember().getMemberId();
        this.nicknameOfWriter = review.getMember().getNickname();
        this.visitCnt = visitCnt;
        this.storeId = review.getStore().getStoreId();
        this.storeName = review.getStore().getStoreName();
        this.content = review.getContent();
        this.regDateTime = review.getRegDateTime();
        this.modDateTime = review.getModDateTime();
        this.reviewImageDtoList = reviewImageDtoList;
    }

}
