package com.dalbong.cafein.service;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.store.StoreRegDto;

public interface StoreService {

    Store register(StoreRegDto storeRegDto, Long principalId);

    void modifyIsApproval(Long storeId);
}
