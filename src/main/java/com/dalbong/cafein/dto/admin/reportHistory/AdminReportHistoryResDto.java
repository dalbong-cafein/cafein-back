package com.dalbong.cafein.dto.admin.reportHistory;

import com.dalbong.cafein.domain.report.ReportStatus;
import com.dalbong.cafein.domain.report.reportHistory.ReportHistory;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminReportHistoryResDto {

    private Long reportHistoryId;

    private Long reportId;

    private ReportStatus reportStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    public AdminReportHistoryResDto(ReportHistory reportHistory){

        this.reportHistoryId = reportHistory.getReportHistoryId();
        this.reportId = reportHistory.getReport().getReportId();
        this.reportStatus = reportHistory.getReportStatus();
        this.regDateTime = reportHistory.getRegDateTime();
    }
}
