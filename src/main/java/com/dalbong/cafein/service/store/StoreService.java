package com.dalbong.cafein.service.store;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.admin.review.AdminReviewListDto;
import com.dalbong.cafein.dto.admin.store.AdminStoreListDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.store.MyStoreResDto;
import com.dalbong.cafein.dto.store.StoreRegDto;
import com.dalbong.cafein.dto.store.StoreResDto;

import java.io.IOException;
import java.util.List;

public interface StoreService {

    Store register(StoreRegDto storeRegDto, Long principal) throws IOException;

    AdminStoreListDto getStoreListOfAdmin(PageRequestDto pageRequestDto);

    List<StoreResDto> getStoreList(String keyword);

    List<MyStoreResDto> getMyStoreList(Long principalId);

}
