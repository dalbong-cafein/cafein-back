package com.dalbong.cafein.web.service;

import com.dalbong.cafein.domain.image.Image;
import com.dalbong.cafein.domain.image.StoreImage;
import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.businessHours.BusinessHoursInfoDto;
import com.dalbong.cafein.dto.image.ImageDto;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.util.DistanceUtil;
import com.dalbong.cafein.web.domain.recommend.Recommend;
import com.dalbong.cafein.web.domain.recommend.RecommendRepository;
import com.dalbong.cafein.web.dto.store.NearStoreResDtoOfWeb;
import com.dalbong.cafein.web.dto.store.StoreResDtoOfWeb;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class StoreServiceOfWeb {

    private final StoreRepository storeRepository;

    /**
     * 웹 - 카페 리스트 조회
     */
    @Transactional(readOnly = true)
    public List<StoreResDtoOfWeb> getStoreListOfWeb(String keyword){

        List<Store> storeList = storeRepository.getStoreListOfWeb(keyword);

        return storeList.stream().map(store -> {

            List<Recommend> recommendList = store.getRecommendList();

            //카페 추천율
            Double recommendPercent = getRecommendPercent(recommendList);

            //카페 영업시간 데이터
            Map<String, Object> businessInfoMap = store.getBusinessInfo();
            BusinessHoursInfoDto businessHoursInfoDto = new BusinessHoursInfoDto(businessInfoMap);

            //이미지 최대 3개 불러오기
            List<ImageDto> storeImageDtoList = new ArrayList<>();

            List<StoreImage> storeImageList = store.getStoreImageList();
            if (storeImageList != null && !storeImageList.isEmpty()) {

                //최신순 조회
                Collections.reverse(storeImageList);

                int count = 0;
                for(StoreImage storeImage : store.getStoreImageList()){
                    storeImageDtoList.add(new ImageDto(storeImage.getImageId(), storeImage.getImageUrl(),
                            storeImage.getIsGoogle(), storeImage.getIsCafein()));
                    count += 1;
                    if(count >= 3) break;
                }
            }

            return new StoreResDtoOfWeb(store, recommendPercent, businessHoursInfoDto, storeImageDtoList);
        }).collect(Collectors.toList());
    }

    /**
     * 근처 카공 카페 리스트 조회 - 조회중인 카페 기준
     */
    @Transactional(readOnly = true)
    public List<NearStoreResDtoOfWeb> getNearStoreList(Long storeId){

        Store findStore = storeRepository.findById(storeId).orElseThrow(() ->
                new CustomException("존재하지 않는 카페입니다."));

        List<Store> storeList = storeRepository.recommendNearStore(storeId, findStore.getLatY(), findStore.getLngX());

        return storeList.stream().map(store -> {

            //리뷰 추천율
            Double recommendPercent = store.getRecommendPercent();

            //카페 영업시간 데이터
            Map<String, Object> businessInfoMap = store.getBusinessInfo();
            BusinessHoursInfoDto businessHoursInfoDto = new BusinessHoursInfoDto(businessInfoMap);

            //이미지 최대 3개 불러오기
            List<ImageDto> storeImageDtoList = new ArrayList<>();

            List<StoreImage> storeImageList = store.getStoreImageList();
            if (storeImageList != null && !storeImageList.isEmpty()) {

                //최신순 조회
                Collections.reverse(storeImageList);
                
                int count = 0;
                for(StoreImage storeImage : store.getStoreImageList()){
                    storeImageDtoList.add(new ImageDto(storeImage.getImageId(), storeImage.getImageUrl(),
                            storeImage.getIsGoogle(), storeImage.getIsCafein()));
                    count += 1;
                    if(count >= 3) break;
                }
            }

            double distance = DistanceUtil.calculateDistance(store.getLatY(), store.getLngX(), findStore.getLatY(), findStore.getLngX(), "meter");

            return new NearStoreResDtoOfWeb(store, recommendPercent, businessHoursInfoDto, storeImageDtoList, null, distance);
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
