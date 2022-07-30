package com.dalbong.cafein.web.service;

import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.web.domain.Recommend;
import com.dalbong.cafein.web.domain.RecommendRepository;
import com.dalbong.cafein.web.dto.RecommendRegDto;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@RestController
public class RecommendService {

    private final RecommendRepository recommendRepository;

    /**
     * 추천 데이터 등록
     */
    @Transactional
    public Recommend recommend(RecommendRegDto recommendRegDto, String sessionId){

        //하루내 등록 여부 체크
        boolean result = recommendRepository.existWithinTime(recommendRegDto.getStoreId(), sessionId);

        if(result){
            throw new CustomException("한 카페에 추천 데이터 등록은 한번만 가능합니다.");
        }

        //추천 데이터 등록
        return recommendRepository.save(recommendRegDto.toEntity(sessionId));
    }

    /**
     * 웹 - 본인의 카페 추천 데이터 조회
     */
    @Transactional
    public Recommendation getRecommendation(Long storeId, String sessionId){

        Optional<Recommend> result = recommendRepository.findByStoreStoreIdAndSessionId(storeId, sessionId);

        if(result.isPresent()){
            return result.get().getRecommendation();
        }else{
            throw new CustomException("등록한 데이터가 존재하지 않습니다.");
        }
    }

}
