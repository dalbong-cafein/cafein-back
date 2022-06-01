package com.dalbong.cafein.dto.admin.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminReviewEvaluationOfStoreResDto {

    private Double recommendPercent;

    private AdminReviewScoreResDto socket;

    private AdminReviewScoreResDto wifi;

    private AdminReviewScoreResDto restroom;

    private AdminReviewScoreResDto tableSize;


}
