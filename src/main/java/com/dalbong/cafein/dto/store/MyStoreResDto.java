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

    private boolean isOpen;

    private int katechX;

    private int katectY;

    private Double congestionScoreAvg;

    private ImageDto storeImageDto;

    public MyStoreResDto(Store store, boolean isOpen, ImageDto storeImageDto, Double congestionScoreAvg){

        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.isOpen = isOpen;
        this.katechX = store.getKatechX();
        this.katectY = store.getKatechY();
        this.congestionScoreAvg = congestionScoreAvg;
        this.storeImageDto = storeImageDto;
    }
}
