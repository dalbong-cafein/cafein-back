package com.dalbong.cafein.dto.store;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.image.ImageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MyRegisterStoreResDto {

    private Long storeId;

    private String storeName;

    private Boolean isOpen;

    private Double congestionScoreAvg;

    private ImageDto storeImageDto;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    public MyRegisterStoreResDto(Store store, Boolean isOpen, Double congestionScoreAvg, ImageDto storeImageDto, Long principalId){

        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.isOpen = isOpen;
        this.congestionScoreAvg = congestionScoreAvg;
        this.storeImageDto = storeImageDto;
        this.regDateTime = store.getRegDateTime();
    }

}
