package com.dalbong.cafein.dto.congestion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CongestionListResDto<T> {

    private long CongestionCnt;

    private T resDtoList;
}
