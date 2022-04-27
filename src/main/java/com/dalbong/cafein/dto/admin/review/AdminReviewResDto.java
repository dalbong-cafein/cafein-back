package com.dalbong.cafein.dto.admin.review;

import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.dto.image.ImageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminReviewResDto {

    private Long reviewId;

    private Long writerId;

    private String nickname;

    private Long storeId;

    private String storeName;

    private String content;

    private ImageDto reviewImageDto;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime modDateTime;

    public AdminReviewResDto(Review review, ImageDto reviewImageDto){
        this.reviewId = review.getReviewId();
        this.writerId = review.getMember().getMemberId();
        this.nickname = review.getMember().getNickname();
        this.content = review.getContent();
        this.reviewImageDto = reviewImageDto;
        this.regDateTime = review.getRegDateTime();
        this.modDateTime = review.getModDateTime();
    }

}
