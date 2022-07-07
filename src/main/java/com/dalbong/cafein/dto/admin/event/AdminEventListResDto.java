package com.dalbong.cafein.dto.admin.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminEventListResDto<T> {

    private long eventCnt;

    private T eventResDtoList;

}
