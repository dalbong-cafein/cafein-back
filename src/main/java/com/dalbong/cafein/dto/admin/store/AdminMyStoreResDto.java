package com.dalbong.cafein.dto.admin.store;

import com.dalbong.cafein.domain.store.Store;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminMyStoreResDto {

    private Long storeId;

    private String storeName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime modDateTime;

    public AdminMyStoreResDto(Store store){

        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.regDateTime = store.getRegDateTime();
        this.modDateTime = store.getModDateTime();
    }

}
