package com.dalbong.cafein.dto.store;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.image.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MyStoreResDto {

    private Long storeId;

    private String storeName;

    private Boolean isOpen;

    private double lngX;

    private double latY;

    private double congestionScoreAvg;

    private ImageDto storeImageDto;

    public MyStoreResDto(Store store, Boolean isOpen, ImageDto storeImageDto, Double congestionScoreAvg){

        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.isOpen = isOpen;
        this.lngX = store.getLngX();
        this.latY = store.getLatY();
        this.congestionScoreAvg = congestionScoreAvg != null ? congestionScoreAvg : 99;
        this.storeImageDto = storeImageDto;
    }
}
