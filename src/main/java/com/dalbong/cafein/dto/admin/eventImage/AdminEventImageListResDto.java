package com.dalbong.cafein.dto.admin.eventImage;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminEventImageListResDto<T> {

    private long eventImageCnt;

    private T eventImageResDtoList;

}
