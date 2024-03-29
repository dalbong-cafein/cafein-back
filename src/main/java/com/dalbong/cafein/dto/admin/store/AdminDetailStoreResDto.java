package com.dalbong.cafein.dto.admin.store;

import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.store.Store;
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
public class AdminDetailStoreResDto {

    private Long storeId;

    private Long modMemberId;

    private String storeName;

    private Address address;

    private String wifiPassword;

    private String phone;

    private String website;

    private TotalBusinessHoursResDto totalBusinessHoursResDto;

    private int viewCnt;

    private int heartCnt;

    private long congestionCnt;

    private int reviewCnt;

    private ImageDto presentImageDto;

    private List<ImageDto> reviewImageDtoList;

    private List<ImageDto> storeImageDtoList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime modDateTime;

    public AdminDetailStoreResDto(Store store, int heartCnt, long congestionCnt, int reviewCnt, ImageDto representImageDto,
                                  List<ImageDto> reviewImageDtoList, List<ImageDto> storeImageDtoList){

        this.storeId = store.getStoreId();
        this.modMemberId = store.getModMember().getMemberId();
        this.storeName = store.getStoreName();
        this.address = store.getAddress();
        this.wifiPassword = store.getWifiPassword();
        this.phone = store.getPhone();
        this.website = store.getWebsite();
        this.totalBusinessHoursResDto = store.getBusinessHours() != null? new TotalBusinessHoursResDto(store.getBusinessHours()) : null;
        this.viewCnt = store.getViewCnt();
        this.heartCnt = heartCnt;
        this.congestionCnt = congestionCnt;
        this.reviewCnt = reviewCnt;
        this.presentImageDto = representImageDto;
        this.reviewImageDtoList = reviewImageDtoList;
        this.storeImageDtoList = storeImageDtoList;
        this.regDateTime = store.getRegDateTime();
        this.modDateTime = store.getInfoModDateTime();


    }

}
