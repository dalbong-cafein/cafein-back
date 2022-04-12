package com.dalbong.cafein.dto.review;

import com.dalbong.cafein.dto.page.ScrollResultDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewListDto {

    private long reviewCnt;

    private ScrollResultDto<ReviewResDto, Object[]> reviewResDtoList;

}
