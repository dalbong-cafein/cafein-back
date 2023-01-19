package com.dalbong.cafein.dto.review;

import com.dalbong.cafein.domain.review.DetailEvaluation;
import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.dto.image.ImageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
public class MyReviewResDto {

    private Long reviewId;

    private String content;

    private long visitCnt;

    private Recommendation recommendation;

    private DetailEvaluation detailEvaluation;

    private Boolean isPost;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    private List<ImageDto> reviewImageDto;

    private Long storeId;

    private String storeName;

    private ImageDto storeImage;

    public MyReviewResDto(Review review, long visitCnt, List<ImageDto> reviewImageDto, ImageDto storeImageDto){

        this.reviewId = review.getReviewId();
        this.content = review.getContent();
        this.visitCnt = visitCnt;
        this.recommendation = review.getRecommendation();
        this.detailEvaluation = review.getDetailEvaluation();
        this.isPost = review.getIsPost();
        this.regDateTime = review.getRegDateTime();
        this.reviewImageDto = reviewImageDto;
        this.storeId = review.getStore().getStoreId();
        this.storeName = review.getStore().getStoreName();
        this.storeImage = storeImageDto;
    }
}
