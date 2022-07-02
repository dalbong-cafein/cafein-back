package com.dalbong.cafein.dto.store;

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
public class StoreResDto {

    private Long storeId;

    private String storeName;

    private Double recommendPercent;

    private BusinessHoursInfoDto businessHoursInfoDto;

    private double lngX;

    private double latY;

    private long heartCnt;

    private double congestionScoreAvg;

    private List<ImageDto> storeImageDtoList;

    public StoreResDto(Store store, Double recommendPercent, BusinessHoursInfoDto businessHoursInfoDto,
                       List<ImageDto> storeImageDtoList, long heartCnt, Double congestionScoreAvg ){

        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.recommendPercent = recommendPercent;
        this.businessHoursInfoDto = businessHoursInfoDto;
        this.lngX = store.getLngX();
        this.latY = store.getLatY();
        this.heartCnt = heartCnt;
        this.congestionScoreAvg = congestionScoreAvg != null ? congestionScoreAvg : 99;
        this.storeImageDtoList = storeImageDtoList;
    }

}
