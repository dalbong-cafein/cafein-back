package com.dalbong.cafein.service.nearStoreToSubwayStation;

import com.dalbong.cafein.domain.nearStoreToSubwayStation.NearStoreToSubwayStation;
import com.dalbong.cafein.domain.nearStoreToSubwayStation.NearStoreToSubwayStationRepository;
import com.dalbong.cafein.domain.store.Store;
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
public class NearStoreToSubWayStationServiceImpl implements NearStoreToSubWayStationService{

    private final NearStoreToSubwayStationRepository nearStoreToSubwayStationRepository;
    private final SubwayStationRepository subwayStationRepository;

    /**
     * 가까운 지하철역 데이터 저장
     */
    @Transactional
    @Override
    public void save(Store store) {

        //서비스 구역내의 지하철역 리스트
        List<SubwayStation> subwayStationList = subwayStationRepository.findByIsUseTrue();

        for (SubwayStation subwayStation : subwayStationList){

            double distance = DistanceUtil.calculateDistance(
                    store.getLatY(), store.getLngX(), subwayStation.getLatY(), subwayStation.getLngX(), "meter");

            //역 거리와 500m 이내
            if(distance <= 500){
                NearStoreToSubwayStation nearStoreToSubwayStation = NearStoreToSubwayStation.builder()
                        .store(store).subwayStation(subwayStation).distance(distance).build();

                nearStoreToSubwayStationRepository.save(nearStoreToSubwayStation);
            }
        }
    }
}
