package com.dalbong.cafein.dto.store;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.businessHours.BusinessHoursInfoDto;
import com.dalbong.cafein.dto.image.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MyStoreResDto {

    private Long storeId;

    private String storeName;

    private BusinessHoursInfoDto businessHoursInfoDto;

    private double lngX;

    private double latY;

    private Double congestionScoreAvg;

    private ImageDto storeImageDto;

    public MyStoreResDto(Store store, BusinessHoursInfoDto businessHoursInfoDto, ImageDto storeImageDto, Double congestionScoreAvg){

        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.businessHoursInfoDto = businessHoursInfoDto;
        this.lngX = store.getLngX();
        this.latY = store.getLatY();
        this.congestionScoreAvg = congestionScoreAvg;
        this.storeImageDto = storeImageDto;
    }
}
