package com.dalbong.cafein.service.store;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.admin.store.AdminStoreListDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.store.*;

import java.io.IOException;
import java.util.List;

public interface StoreService {

    Store register(StoreRegDto storeRegDto, Long principal) throws IOException;

    void modify(StoreUpdateDto storeUpdateDto);

    List<StoreResDto> getStoreList(String keyword);

    StoreListResDto<List<MyStoreResDto>> getMyStoreList(Long principalId);

    StoreListResDto<List<MyRegisterStoreResDto>> getMyRegisterStoreList(Long principalId);

    List<RecommendSearchStoreResDto> getRecommendSearchStoreList(String keyword);

    DetailStoreResDto getDetailStore(Long storeId, Long principalId);

    AdminStoreListDto getStoreListOfAdmin(PageRequestDto pageRequestDto);

}
