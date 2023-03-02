package com.dalbong.cafein.dto.notice.detailReportNotice;

import com.dalbong.cafein.domain.review.DetailEvaluation;
import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.dto.image.ImageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReportedReviewResDto {

    private Long reviewId;

    private String content;

    private Recommendation recommendation;

    private DetailEvaluation detailEvaluation;

    private List<ImageDto> reviewImageDtoList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime stopPostDateTime;

    private Long storeId;

    private String storeName;

    private ImageDto storeImageDto;

    public ReportedReviewResDto(Review review, List<ImageDto> reviewImageDtoList, ImageDto storeImageDto, LocalDateTime stopPostDateTime){

        this.reviewId = review.getReviewId();
        this.content = review.getContent();
        this.recommendation = review.getRecommendation();
        this.detailEvaluation = review.getDetailEvaluation();
        this.reviewImageDtoList = reviewImageDtoList;
        this.regDateTime = review.getRegDateTime();
        this.stopPostDateTime = stopPostDateTime;
        this.storeId = review.getStore().getStoreId();
        this.storeName = review.getStore().getStoreName();
        this.storeImageDto = storeImageDto;
    }

}
