package com.dalbong.cafein.web.dto.recommend;

import com.dalbong.cafein.domain.review.Recommendation;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RecommendResDto {

    private Double recommendPercentOfStore;

    private CountByRecommendTypeResDto countByRecommendTypeResDto;

    private Recommendation regRecommendation;

}
