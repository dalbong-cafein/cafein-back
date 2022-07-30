package com.dalbong.cafein.web.dto;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.businessHours.BusinessHoursInfoDto;
import com.dalbong.cafein.dto.image.ImageDto;
import lombok.Data;

import java.util.List;

@Data
public class StoreResDtoOfWeb {

    private Long storeId;

    private String storeName;

    private String fullAddress;

    private Double recommendPercent;

    private BusinessHoursInfoDto businessHoursInfoDto;

    private double lngX;

    private double latY;

    private ImageDto storeImageDto;

    public StoreResDtoOfWeb(Store store, Double recommendPercent, BusinessHoursInfoDto businessHoursInfoDto,
                            ImageDto storeImageDto){

        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.fullAddress = store.getAddress().getFullAddress();
        this.recommendPercent = recommendPercent;
        this.businessHoursInfoDto = businessHoursInfoDto;
        this.lngX = store.getLngX();
        this.latY = store.getLatY();
        this.storeImageDto = storeImageDto;
    }
}
