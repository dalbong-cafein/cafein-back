package com.dalbong.cafein.dto.review;

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
public class ReviewResDto {

    private Long reviewId;

    private Long writerId;

    private String nicknameOfWriter;

    private String profileImageUrl;

    private String content;

    private long visitCnt;

    private List<ImageDto> reviewImageDtoList = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    public ReviewResDto(Review review, String profileImageUrl, long visitCnt, List<ImageDto> reviewImageDtoList){

        this.reviewId = review.getReviewId();
        this.writerId = review.getMember().getMemberId();
        this.nicknameOfWriter = review.getMember().getNickname();
        this.profileImageUrl = profileImageUrl;
        this.content = review.getContent();
        this.visitCnt = visitCnt;
        this.reviewImageDtoList = reviewImageDtoList;
        this.regDateTime = review.getRegDateTime();
    }

}
