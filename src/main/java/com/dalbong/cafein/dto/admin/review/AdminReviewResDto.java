package com.dalbong.cafein.dto.admin.review;

import com.dalbong.cafein.domain.review.DetailEvaluation;
import com.dalbong.cafein.domain.review.Recommendation;
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

    private String nicknameOfWriter;

    private Long storeId;

    private String storeName;

    private Recommendation recommendation;

    private DetailEvaluation detailEvaluation;

    private String content;

    private Long memoId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime modDateTime;

    public AdminReviewResDto(Review review, Long memoId){
        this.reviewId = review.getReviewId();
        this.writerId = review.getMember().getMemberId();
        this.nicknameOfWriter = review.getMember().getNickname();
        this.storeId = review.getStore().getStoreId();
        this.storeName = review.getStore().getStoreName();
        this.recommendation = review.getRecommendation();
        this.detailEvaluation = review.getDetailEvaluation();
        this.content = review.getContent();
        this.regDateTime = review.getRegDateTime();
        this.modDateTime = review.getModDateTime();
        this.memoId = memoId;
    }

}
