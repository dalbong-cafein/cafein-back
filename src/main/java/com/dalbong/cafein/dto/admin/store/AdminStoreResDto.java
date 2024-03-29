package com.dalbong.cafein.dto.admin.store;

import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.image.ImageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminStoreResDto {

    private Long storeId;

    private String storeName;

    private Address address;

    private String phone;

    private int reviewCnt;

    private Double recommendPercent;

    private Double congestionScoreAvg;

    private ImageDto storeImageDto;

    private Long regMemberId;

    private Long modMemberId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime modDateTime;

    private Long memoId;

    public AdminStoreResDto(Store store, int reviewCnt,Double recommendPercent, Double congestionScoreAvg, ImageDto storeImageDto, Long memoId){

        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.address = store.getAddress();
        this.phone = store.getPhone();
        this.congestionScoreAvg = congestionScoreAvg;
        this.reviewCnt = reviewCnt;
        this.recommendPercent = recommendPercent;
        this.storeImageDto = storeImageDto;
        this.regMemberId = store.getRegMember().getMemberId();
        this.modMemberId = store.getModMember().getMemberId();
        this.regDateTime = store.getRegDateTime();
        this.modDateTime = store.getInfoModDateTime();
        this.memoId = memoId;
    }
}
