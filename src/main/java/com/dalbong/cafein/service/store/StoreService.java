package com.dalbong.cafein.service.store;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.store.StoreRegDto;

import java.io.IOException;

public interface StoreService {

    Store register(StoreRegDto storeRegDto, Long principal) throws IOException;

}
