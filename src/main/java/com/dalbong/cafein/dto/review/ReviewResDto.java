package com.dalbong.cafein.dto.review;

import com.dalbong.cafein.domain.review.Review;
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

    private String nickname;

    private String profileImageUrl;

    private String content;

    private long visitCnt;
    
    private List<String> reviewImageUrlList = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    public ReviewResDto(Review review, String profileImageUrl, long visitCnt, List<String> reviewImageUrlList){

        this.reviewId = review.getReviewId();
        this.writerId = review.getMember().getMemberId();
        this.nickname = review.getMember().getNickname();
        this.profileImageUrl = profileImageUrl;
        this.content = review.getContent();
        this.visitCnt = visitCnt;
        this.reviewImageUrlList = reviewImageUrlList;
        this.regDateTime = review.getRegDate();
    }

}
