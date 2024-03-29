package com.dalbong.cafein.service.heart;

import com.dalbong.cafein.domain.heart.Heart;

public interface HeartService {

    Heart heart(Long principalId, Long storeId);

    void cancelHeart(Long principalId, Long storeId);
}
