package com.dalbong.cafein.service.store;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.admin.store.AdminDetailStoreResDto;
import com.dalbong.cafein.dto.admin.store.AdminMyStoreResDto;
import com.dalbong.cafein.dto.admin.store.AdminStoreListDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.store.*;

import java.io.IOException;
import java.util.List;

public interface StoreService {

    Store register(StoreRegDto storeRegDto, Long principalId) throws IOException;

    void modify(StoreUpdateDto storeUpdateDto, Long principalId) throws IOException;

    void remove(Long storeId);

    List<StoreResDto> getStoreList(StoreSearchRequestDto storeSearchRequestDto, Long principalId);

    StoreListResDto<List<MyStoreResDto>> getMyStoreList(Long principalId);

    StoreListResDto<List<MyStoreResDto>>getCustomLimitMyStoreList(int limit, Long principalId);

    StoreListResDto<List<MyRegisterStoreResDto>> getMyRegisterStoreList(Long principalId);

    List<NearStoreResDto> getNearStoreListOfStore(Long storeId, Long principalId);

    DetailStoreResDto getDetailStore(Long storeId, Long principalId);

    Store registerOfAdmin(StoreRegDto storeRegDto, Long principalId) throws IOException;

    void modifyOfAdmin(StoreUpdateDto storeUpdateDto, Long principalId) throws IOException;

    AdminStoreListDto getStoreListOfAdmin(PageRequestDto pageRequestDto);

    AdminDetailStoreResDto getDetailStoreOfAdmin(Long storeId);

    List<AdminMyStoreResDto> getMyStoreByMemberIdOfAdmin(Long memberId);

    Long getRegisterCountOfToday();

    int countMyRegisterStore(Long memberId);

}
