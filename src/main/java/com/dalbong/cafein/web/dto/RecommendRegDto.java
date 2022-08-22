package com.dalbong.cafein.web.dto;

import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.web.domain.recommend.Recommend;
import lombok.Data;

@Data
public class RecommendRegDto {

    private Long storeId;

    private Recommendation recommendation;

    public Recommend toEntity(String sessionId){

        return Recommend.builder()
                .store(Store.builder().storeId(this.storeId).build())
                .sessionId(sessionId)
                .recommendation(this.recommendation)
                .build();

    }


}
