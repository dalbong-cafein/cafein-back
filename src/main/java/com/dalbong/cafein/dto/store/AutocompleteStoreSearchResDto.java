package com.dalbong.cafein.dto.store;

import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.store.Store;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AutocompleteStoreSearchResDto {

    private Long storeId;

    private String storeName;

    private String fullAddress;

    public AutocompleteStoreSearchResDto(Store store){

        this.storeId = store.getStoreId();
        this.storeName = store.getStoreName();
        this.fullAddress = store.getAddress().getFullAddress();
    }
}
