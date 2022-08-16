package com.dalbong.cafein.web.dto;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.businessHours.BusinessHoursInfoDto;
import com.dalbong.cafein.dto.image.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NearStoreResDtoOfWeb {

    private Long storeId;

    private String storeName;

    private Double recommendPercent;

    private BusinessHoursInfoDto businessHoursInfoDto;

    private double lngX;

    private double latY;

    private long heartCnt;

    private Double congestionScoreAvg;

    private Double distance;

    private List<ImageDto> storeImageDtoList;

    public NearStoreResDtoOfWeb(Store store, Double recommendPercent, BusinessHoursInfoDto businessHoursInfoDto,
                                List<ImageDto> storeImageDtoList, Double congestionScoreAvg, double distance){

        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.recommendPercent = recommendPercent;
        this.businessHoursInfoDto = businessHoursInfoDto;
        this.lngX = store.getLngX();
        this.latY = store.getLatY();
        this.heartCnt = store.getHeartList().size();
        this.congestionScoreAvg = congestionScoreAvg;
        this.distance = distance;
        this.storeImageDtoList = storeImageDtoList;
    }
}
