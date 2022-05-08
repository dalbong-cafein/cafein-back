package com.dalbong.cafein.dto.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StoreListResDto<T> {

    private long storeCnt;

    private T resDtoList;

}
