package com.dalbong.cafein.dto.store;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.businessHours.BusinessHoursInfoDto;
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

    private BusinessHoursInfoDto businessHoursInfoDto;

    private double congestionScoreAvg;

    private ImageDto storeImageDto;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    public MyRegisterStoreResDto(Store store, BusinessHoursInfoDto businessHoursInfoDto,
                                 Double congestionScoreAvg, ImageDto storeImageDto){

        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.businessHoursInfoDto = businessHoursInfoDto;
        this.congestionScoreAvg = congestionScoreAvg != null ? congestionScoreAvg : 99;
        this.storeImageDto = storeImageDto;
        this.regDateTime = store.getRegDateTime();
    }

}
