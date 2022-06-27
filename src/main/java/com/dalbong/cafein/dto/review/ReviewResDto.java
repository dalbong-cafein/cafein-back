package com.dalbong.cafein.dto.review;

import com.dalbong.cafein.domain.review.DetailEvaluation;
import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.dto.image.ImageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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

    private Recommendation recommendation;

    private DetailEvaluation detailEvaluation;

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
        this.recommendation = review.getRecommendation();
        this.detailEvaluation = review.getDetailEvaluation();
        this.reviewImageDtoList = reviewImageDtoList;
        this.regDateTime = review.getRegDateTime();
    }

}
