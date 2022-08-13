package com.dalbong.cafein.dto.store;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.businessHours.BusinessHoursInfoDto;
import com.dalbong.cafein.dto.image.ImageDto;
import com.dalbong.cafein.util.DistanceUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NearStoreResDto {

    private Long storeId;

    private String storeName;

    private Double recommendPercent;

    private BusinessHoursInfoDto businessHoursInfoDto;

    private double lngX;

    private double latY;

    private long heartCnt;

    private Boolean isHeart;

    private Double congestionScoreAvg;

    private Double distance;

    private List<ImageDto> storeImageDtoList;

    public NearStoreResDto(Store store, Double recommendPercent, BusinessHoursInfoDto businessHoursInfoDto,
                       List<ImageDto> storeImageDtoList, Double congestionScoreAvg, double distance, Long principalId){

        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.recommendPercent = recommendPercent;
        this.businessHoursInfoDto = businessHoursInfoDto;
        this.lngX = store.getLngX();
        this.latY = store.getLatY();
        this.heartCnt = store.getHeartList().size();
        this.isHeart = store.getHeartList().stream().anyMatch(h -> h.getMember().getMemberId().equals(principalId) ? true : false);
        this.congestionScoreAvg = congestionScoreAvg;
        this.distance = distance;
        this.storeImageDtoList = storeImageDtoList;
    }
}
