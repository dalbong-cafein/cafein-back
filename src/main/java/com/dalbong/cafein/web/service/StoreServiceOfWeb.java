package com.dalbong.cafein.web.service;

import com.dalbong.cafein.domain.image.Image;
import com.dalbong.cafein.domain.image.StoreImage;
import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.businessHours.BusinessHoursInfoDto;
import com.dalbong.cafein.dto.image.ImageDto;
import com.dalbong.cafein.dto.store.RecommendSearchStoreResDto;
import com.dalbong.cafein.dto.store.StoreResDto;
import com.dalbong.cafein.web.domain.Recommend;
import com.dalbong.cafein.web.domain.RecommendRepository;
import com.dalbong.cafein.web.dto.StoreResDtoOfWeb;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class StoreServiceOfWeb {

    private final StoreRepository storeRepository;
    private final RecommendRepository recommendRepository;

    /**
     * 웹 - 카페 리스트 조회
     */
    @Transactional(readOnly = true)
    public List<StoreResDtoOfWeb> getStoreListOfWeb(String keyword){

        List<Store> storeList = storeRepository.getStoreListOfWeb(keyword);

        return storeList.stream().map(store -> {

            List<Recommend> recommendList = recommendRepository.findByStoreId(store.getStoreId());

            //카페 추천율
            Double recommendPercent = getRecommendPercent(recommendList);

            //카페 영업시간 데이터
            Map<String, Object> businessInfoMap = store.getBusinessInfo();
            BusinessHoursInfoDto businessHoursInfoDto = new BusinessHoursInfoDto(businessInfoMap);

            //첫번째 이미지 불러오기
            ImageDto storeImageDto = null;
            if (store.getStoreImageList() != null && !store.getStoreImageList().isEmpty()) {
                StoreImage storeImage = store.getStoreImageList().get(0);
                storeImageDto = new ImageDto(storeImage.getImageId(), storeImage.getImageUrl());
            }

            return new StoreResDtoOfWeb(store, recommendPercent, businessHoursInfoDto, storeImageDto);
        }).collect(Collectors.toList());
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
