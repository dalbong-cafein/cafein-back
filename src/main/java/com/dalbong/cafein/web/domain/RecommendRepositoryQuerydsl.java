package com.dalbong.cafein.web.domain;

public interface RecommendRepositoryQuerydsl {

    boolean existWithinTime(Long storeId, String sessionId);

}
