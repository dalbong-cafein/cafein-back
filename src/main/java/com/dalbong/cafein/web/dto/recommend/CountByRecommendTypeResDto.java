package com.dalbong.cafein.web.dto.recommend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CountByRecommendTypeResDto {

    private long bad = 0;

    private long normal = 0;

    private long good = 0;


}
