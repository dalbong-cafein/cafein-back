package com.dalbong.cafein.dataSet.review;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.DetailEvaluation;
import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.store.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class ExcelReviewDataDto {

    private String storeName;

    private Integer socket;

    private Integer wifi;

    private Integer restroom;

    private Integer tableSize;

    private Recommendation recommendation;

    private String content;

    public Review toReview(Store store, Member member){

        DetailEvaluation detailEvaluation =
                new DetailEvaluation(this.socket, this.wifi, this.restroom, this.tableSize);

        Review review = Review.builder()
                .member(member)
                .content("")
                .detailEvaluation(detailEvaluation)
                .recommendation(this.recommendation)
                .content(this.content)
                .build();

        review.setStore(store);

        return review;
    }

}
