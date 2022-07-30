package com.dalbong.cafein.web.service;

import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.web.domain.Recommend;
import com.dalbong.cafein.web.domain.RecommendRepository;
import com.dalbong.cafein.web.dto.RecommendRegDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RecommendService {

    private final RecommendRepository recommendRepository;

    /**
     * 추천 데이터 등록
     */
    public Recommend recommend(RecommendRegDto recommendRegDto, String sessionId){

        //하루내 등록 여부 체크
        boolean result = recommendRepository.existWithinTime(recommendRegDto.getStoreId(), sessionId);

        if(result){
            throw new CustomException("한 카페에 추천 데이터 등록은 한번만 가능합니다.");
        }

        //추천 데이터 등록
        return recommendRepository.save(recommendRegDto.toEntity(sessionId));
    }

}
