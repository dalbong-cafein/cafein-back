package com.dalbong.cafein.dto.store;

import com.dalbong.cafein.domain.address.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AutocompleteStoreSearchResDto {

    private Long storeId;

    private String storeName;

    //TODO fullAddress 로 대체 검토
    private Address address;
}
