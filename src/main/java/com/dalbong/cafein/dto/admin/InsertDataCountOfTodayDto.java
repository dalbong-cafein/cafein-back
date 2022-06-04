package com.dalbong.cafein.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InsertDataCountOfTodayDto {

    int storeCnt;

    int memberCnt;

    int reviewCnt;
}
