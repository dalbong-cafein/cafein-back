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

    private Double userDistance;

    private long heartCnt;

    private Boolean isHeart;

    private Double congestionScoreAvg;

    private List<ImageDto> storeImageDtoList;

    public StoreResDto(Store store, Double recommendPercent, BusinessHoursInfoDto businessHoursInfoDto, Double userDistance,
                       List<ImageDto> storeImageDtoList, long heartCnt, Double congestionScoreAvg, Long principalId){

        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.recommendPercent = recommendPercent;
        this.businessHoursInfoDto = businessHoursInfoDto;
        this.userDistance = userDistance;
        this.lngX = store.getLngX();
        this.latY = store.getLatY();
        this.heartCnt = heartCnt;
        this.isHeart = store.getHeartList().stream().anyMatch(h -> h.getMember().getMemberId().equals(principalId) ? true : false);
        this.congestionScoreAvg = congestionScoreAvg;
        this.storeImageDtoList = storeImageDtoList;
    }
}
