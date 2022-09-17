package com.dalbong.cafein.dto.store;

import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.businessHours.BusinessHoursInfoDto;
import com.dalbong.cafein.dto.businessHours.TotalBusinessHoursResDto;
import com.dalbong.cafein.dto.image.ImageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DetailStoreResDto {

    private Long storeId;

    private String storeName;

    private String nicknameOfModMember;

    private ImageDto memberImageDto;

    private Address address;

    private String wifiPassword;

    private int heartCnt;

    private Boolean isHeart;

    private Double congestionScoreAvg;

    private BusinessHoursInfoDto businessHoursInfoDto;

    private TotalBusinessHoursResDto totalBusinessHoursResDto;

    private double lngX;

    private double latY;

    private List<ImageDto> reviewImageList;

    private List<ImageDto> storeImageList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime modDateTime;

    public DetailStoreResDto(Store store, Double congestionScoreAvg, ImageDto memberImageDto,
                             List<ImageDto> reviewImageDtoList, List<ImageDto> storeImageDtoList,Long principalId){

        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.nicknameOfModMember = store.getModMember().getNickname();
        this.memberImageDto = memberImageDto;
        this.address = store.getAddress();
        this.wifiPassword = store.getWifiPassword();
        this.heartCnt = store.getHeartList().size();
        this.isHeart = store.getHeartList().stream().anyMatch(h -> h.getMember().getMemberId().equals(principalId) ? true : false);
        this.businessHoursInfoDto = new BusinessHoursInfoDto(store.getBusinessInfo());
        this.totalBusinessHoursResDto = store.getBusinessHours() != null? new TotalBusinessHoursResDto(store.getBusinessHours()) : null;
        this.congestionScoreAvg = congestionScoreAvg;
        this.lngX = store.getLngX();
        this.latY = store.getLatY();
        this.reviewImageList = reviewImageDtoList;
        this.storeImageList = storeImageDtoList;
        this.regDateTime = store.getRegDateTime();
        this.modDateTime = store.getModDateTime();

    }
}
