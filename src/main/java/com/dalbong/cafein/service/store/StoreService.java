package com.dalbong.cafein.service.store;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.store.StoreRegDto;

import java.io.IOException;

public interface StoreService {

    Store registerByAdmin(StoreRegDto storeRegDto) throws IOException;

    void modifyIsApproval(Long storeId);
}
