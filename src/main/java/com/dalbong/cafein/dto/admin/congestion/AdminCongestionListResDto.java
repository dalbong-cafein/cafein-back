package com.dalbong.cafein.dto.admin.congestion;

import lombok.Data;

@Data
public class AdminCongestionListResDto<T> {

    private int congestionCnt;

    private T congestionResDtoList;
}
