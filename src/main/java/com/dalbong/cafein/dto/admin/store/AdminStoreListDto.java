package com.dalbong.cafein.dto.admin.store;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.page.PageResultDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminStoreListDto {

    private long storeCnt;

    private PageResultDTO<AdminStoreResDto, Object[]> storeResDtoList;

}
