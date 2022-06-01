package com.dalbong.cafein.dto.admin.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminReportListResDto {

    private int reportCnt;

    List<AdminReportResDto> adminReportResDtoList;

}
