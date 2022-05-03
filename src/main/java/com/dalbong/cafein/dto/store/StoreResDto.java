package com.dalbong.cafein.dto.store;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.image.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoreResDto {

    private Long storeId;

    private String storeName;

    private double recommendPercent;

    private boolean isOpen;

    private int katechX;

    private int katectY;

    private long heartCnt;

    private Double congestionScoreAvg;

    private ImageDto storeImageDto;

    public StoreResDto(Store store, double recommendPercent, boolean isOpen, ImageDto storeImageDto, long heartCnt, Double congestionScoreAvg ){

        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.recommendPercent = recommendPercent;
        this.isOpen = isOpen;
        this.katechX = store.getKatechX();
        this.katectY = store.getKatechY();
        this.heartCnt = heartCnt;
        this.congestionScoreAvg = congestionScoreAvg;
        this.storeImageDto = storeImageDto;
    }

}
