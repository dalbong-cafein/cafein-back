package com.dalbong.cafein.dataSet;

import com.dalbong.cafein.domain.nearStoreToSubwayStation.NearStoreToSubwayStation;
import com.dalbong.cafein.domain.nearStoreToSubwayStation.NearStoreToSubwayStationRepository;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.domain.subwayStation.SubwayStation;
import com.dalbong.cafein.domain.subwayStation.SubwayStationRepository;
import com.dalbong.cafein.util.DistanceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class SubwayStationService {

    private final SubwayStationRepository subwayStationRepository;
    private final StoreRepository storeRepository;
    private final NearStoreToSubwayStationRepository nearStoreToSubwayStationRepository;

    /**
     * 지하철역 데이터 저장
     */
    @Transactional
    public void save(List<SubwayStationDto> subwayStationDtoList){

        for(SubwayStationDto subwayStationDto : subwayStationDtoList){

            //기존 저정 여부 확인
            boolean result = subwayStationRepository.existsByStationId(subwayStationDto.getStatn_id());

            if(!result){
                //데이터 저장
                SubwayStation subwayStation = subwayStationDto.toEntity();
                subwayStationRepository.save(subwayStation);
            }else{
                System.out.println(subwayStationDto.getStatn_nm() + "은 이미 존재하는 데이터입니다.");
            }
        }
    }


    /**
     *  역과 가까운 카페 데이터 저장
     */
    @Transactional
    public void saveNearStoreToSubwayStation(){

        List<Store> storeList = storeRepository.findAll();
        List<SubwayStation> subwayStationList = subwayStationRepository.findByIsUseTrue();

        for(Store store : storeList){
            for(SubwayStation subwayStation : subwayStationList){

                double distance = DistanceUtil.calculateDistance(store.getLatY(), store.getLngX(), subwayStation.getLatY(), subwayStation.getLngX(), "meter");

                if(distance <= 500){

                    boolean exist = nearStoreToSubwayStationRepository.existsByStoreAndSubwayStation(store, subwayStation);

                    if(!exist){
                        NearStoreToSubwayStation nearStoreToSubwayStation = NearStoreToSubwayStation.builder()
                                .store(store)
                                .subwayStation(subwayStation)
                                .distance(distance).build();

                        nearStoreToSubwayStationRepository.save(nearStoreToSubwayStation);
                    }


                }

            }
        }


    }


}
