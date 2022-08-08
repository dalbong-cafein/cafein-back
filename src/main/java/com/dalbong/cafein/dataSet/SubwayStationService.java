package com.dalbong.cafein.dataSet;

import com.dalbong.cafein.domain.subwayStation.SubwayStation;
import com.dalbong.cafein.domain.subwayStation.SubwayStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class SubwayStationService {

    private final SubwayStationRepository subwayStationRepository;

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


}
