package com.dalbong.cafein.dto.admin.congestion;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AdminCongestionListResDto<T> {

    private int congestionCnt;

    private T congestionResDtoList;
}
