package com.dalbong.cafein.service.reportCategory;

import com.dalbong.cafein.domain.reportCategory.ReportCategory;
import com.dalbong.cafein.domain.reportCategory.ReportCategoryRepository;
import com.dalbong.cafein.dto.reportCategory.ReportCategoryResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class ReportCategoryService {

    private final ReportCategoryRepository reportCategoryRepository;

    /**
     * 신고사유 카테고리 조회
     */
    @Transactional(readOnly = true)
    public List<ReportCategoryResDto> getReportCategoryList(){

        List<ReportCategory> reportCategoryList = reportCategoryRepository.findAll();

        return reportCategoryList.stream().map(reportCategory ->
                new ReportCategoryResDto(reportCategory.getReportCategoryId(), reportCategory.getCategoryName()))
                .collect(Collectors.toList());
    }

}
