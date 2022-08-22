package com.dalbong.cafein.web.domain.recommend;

public interface RecommendRepositoryQuerydsl {

    boolean existWithinTime(Long storeId, String sessionId);

}
