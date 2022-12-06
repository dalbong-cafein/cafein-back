package com.dalbong.cafein.dataSet.university;

import com.dalbong.cafein.dataSet.store.naver.NaverCloudDto;
import com.dalbong.cafein.dataSet.store.naver.NaverCloudService;
import com.dalbong.cafein.dataSet.subwayStation.SubwayStationDto;
import com.dalbong.cafein.domain.nearStoreToSubwayStation.NearStoreToSubwayStation;
import com.dalbong.cafein.domain.nearStoreToUniversity.NearStoreToUniversity;
import com.dalbong.cafein.domain.nearStoreToUniversity.NearStoreToUniversityRepository;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.domain.subwayStation.SubwayStation;
import com.dalbong.cafein.domain.university.University;
import com.dalbong.cafein.domain.university.UniversityRepository;
import com.dalbong.cafein.util.DistanceUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final StoreRepository storeRepository;
    private final NearStoreToUniversityRepository nearStoreToUniversityRepository;
    private final NaverCloudService naverCloudService;

    /**
     * 대학교 데이터 저장
     */
    @Transactional
    public void save(List<UniversityDto> universityDtoList) throws JsonProcessingException {

        for (UniversityDto universityDto : universityDtoList) {

            //기존 저장 여부 확인
            boolean isExist = universityRepository.existsByMainKey(universityDto.getMain_key());

            if (!isExist) {

                //대학교 위치 데이터 얻기
                NaverCloudDto naverCloudDto = naverCloudService.getLatAndLng(universityDto.getAdd_kor());

                //데이터 저장
                University university = universityDto.toEntity(naverCloudDto.getX(), naverCloudDto.getY());

                universityRepository.save(university);
            } else {
                System.out.println(universityDto.getName_kor() + "은 이미 존재하는 데이터 입니다.");
            }
        }
    }

    /**
     *  대학교와 가까운 카페 데이터 저장
     */
    @Transactional
    public void saveNearStoreToUniversity(){

        List<Store> storeList = storeRepository.findAll();

        List<University> universityList = universityRepository.findByIsUseTrue();

        for(University university : universityList){
            for(Store store : storeList){

                double distance = DistanceUtil.calculateDistance(
                        store.getLatY(), store.getLngX(), university.getLatY(), university.getLngX(), "meter");

                if(distance <= 1000){
                    boolean exist = nearStoreToUniversityRepository.existsByStoreAndUniversity(store, university);

                    if(!exist){
                        NearStoreToUniversity nearStoreToUniversity = NearStoreToUniversity.builder()
                                .university(university)
                                .store(store)
                                .distance(distance)
                                .build();

                        nearStoreToUniversityRepository.save(nearStoreToUniversity);
                    }
                }

            }
        }
    }
}
