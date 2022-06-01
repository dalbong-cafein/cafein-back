package com.dalbong.cafein.dto.store;

import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.businessHours.BusinessHoursResDto;
import com.dalbong.cafein.dto.image.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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

    private Boolean isOpen;

    private BusinessHoursResDto businessHoursResDto;

    private double lngX;

    private double latY;

    //reviewImage + storeImage
    private List<ImageDto> totalImageDtoList;

    public DetailStoreResDto(Store store, ImageDto memberImageDto, List<ImageDto> totalImageDtoList, Long principalId){

        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.nicknameOfModMember = store.getModMember().getNickname();
        this.memberImageDto = memberImageDto;
        this.address = store.getAddress();
        this.wifiPassword = store.getWifiPassword();
        this.heartCnt = store.getHeartList().size();
        this.isHeart = store.getHeartList().stream().anyMatch(h -> h.getMember().getMemberId().equals(principalId) ? true : false);
        this.isOpen = store.checkIsOpen();
        this.businessHoursResDto = store.getBusinessHours() != null? new BusinessHoursResDto(store.getBusinessHours()) : null;
        this.lngX = store.getLngX();
        this.latY = store.getLatY();
        this.totalImageDtoList = totalImageDtoList;

    }
}
