package com.dalbong.cafein.dto.admin.report;

import com.dalbong.cafein.domain.report.Report;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminReportResDto {

    private Long reportId;

    private Long reportCategoryId;

    private String categoryName;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    public AdminReportResDto(Report report){
        this.reportId = report.getReportId();
        this.reportCategoryId = report.getReportCategory() != null ? report.getReportCategory().getReportCategoryId() : null;
        this.categoryName = report.getReportCategory() != null ? report.getReportCategory().getCategoryName() : null;
        this.content = report.getContent() != null ? report.getContent() : null;
        this.regDateTime = report.getRegDateTime();
    }
}
