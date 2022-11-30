package com.dalbong.cafein.domain.store.dto;

import com.dalbong.cafein.domain.store.Store;
import lombok.Data;

@Data
public class StoreSortDto {

    private Store store;

    private int heartCnt;

    private int reviewCnt;

    private Boolean isExistImage;

    private Double congestionAvg;

    private Double distance;

    public StoreSortDto(Store store, int heartCnt, int reviewCnt, boolean isExistImage, Double congestionAvg){
        this.store = store;
        this.heartCnt = heartCnt;
        this.reviewCnt = reviewCnt;
        this.isExistImage = isExistImage;
        this.congestionAvg = congestionAvg;
    }


}
