package com.dalbong.cafein.web.service;

import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.web.domain.Recommend;
import com.dalbong.cafein.web.domain.RecommendRepository;
import com.dalbong.cafein.web.dto.RecommendRegDto;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
     * 웹 - 본인이 등록한 카페 추천 데이터 조회
     */
    @Transactional(readOnly = true)
    public Optional<Recommendation> getRecommendation(Long storeId, String sessionId){

        Recommend recommend = recommendRepository.findByStoreStoreIdAndSessionId(storeId, sessionId);

        if(recommend != null){
            return Optional.of(recommend.getRecommendation());
        }else{
            return Optional.empty();
        }
    }

    /**
     * 웹 - 카페 추천율 조회
     */
    @Transactional(readOnly = true)
    public Double getRecommendPercent(Long storeId){

        List<Recommend> recommendList = recommendRepository.findByStoreId(storeId);

        return getRecommendPercent(recommendList);
    }

    private Double getRecommendPercent(List<Recommend> recommendList) {

        int totalSize = recommendList.size();

        //추천 데이터가 없을 경우
        if (totalSize == 0){
            return null;
        }

        double recommendCnt = 0;

        for(Recommend recommend : recommendList){
            if(recommend.getRecommendation().equals(Recommendation.GOOD)){
                recommendCnt += 1;
            }
        }

        return (recommendCnt / totalSize) * 100;
    }

}
