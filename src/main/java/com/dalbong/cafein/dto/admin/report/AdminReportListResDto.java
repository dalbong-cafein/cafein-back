package com.dalbong.cafein.dto.admin.report;

import com.dalbong.cafein.dto.admin.store.AdminStoreResDto;
import com.dalbong.cafein.dto.page.PageResultDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminReportListResDto<T> {

    private long reportCnt;

    private T reportResDtoList;
}
