package com.dalbong.cafein.dto.store;

import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.store.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RecommendSearchStoreResDto {

    private Long storeId;

    private String storeName;

    private Address address;

    public RecommendSearchStoreResDto(Store store){
        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.address = store.getAddress();
    }
}
