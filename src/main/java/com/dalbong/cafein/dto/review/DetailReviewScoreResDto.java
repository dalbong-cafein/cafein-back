package com.dalbong.cafein.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DetailReviewScoreResDto {

    private int reviewCnt;

    private Double recommendPercent;

    private String socket;

    private int socketCnt;

    private String wifi;

    private int wifiCnt;

    private String restroom;

    private int restroomCnt;

    private String tableSize;

    private int tableCnt;

}
