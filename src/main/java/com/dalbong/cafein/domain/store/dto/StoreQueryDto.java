package com.dalbong.cafein.domain.store.dto;

import com.dalbong.cafein.domain.store.Store;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StoreQueryDto {

    private Store store;

    private Integer heartCnt;

    private Double congestionAvg;

    private Double userDistance;

    public StoreQueryDto(Store store, Integer heartCnt, Double congestionAvg){
        this.store = store;
        this.heartCnt = heartCnt;
        this.congestionAvg = congestionAvg;
    }
}
