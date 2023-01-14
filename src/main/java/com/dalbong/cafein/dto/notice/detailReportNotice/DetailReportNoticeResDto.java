package com.dalbong.cafein.dto.notice.detailReportNotice;

import com.dalbong.cafein.domain.notice.ReportNotice;
import com.dalbong.cafein.domain.notice.detailReportNotice.DetailReportNotice;
import com.dalbong.cafein.dto.reportCategory.ReportCategoryResDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DetailReportNoticeResDto {

    private Long noticeId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime reportExpiredDateTime;

    private String reportCategoryName;

    private ReportedReviewResDto reportedReviewResDto;

    private Boolean isPossibleObjection;

    public DetailReportNoticeResDto(ReportNotice reportNotice, ReportedReviewResDto reportedReviewResDto){
        this.noticeId = reportNotice.getNoticeId();
        this.reportExpiredDateTime = reportNotice.getDetailReportNotice().getReportExpiredDateTime() != null ?
                reportNotice.getDetailReportNotice().getReportExpiredDateTime().minusDays(1).toLocalDate().atTime(23,59,59) : null;
        this.reportCategoryName = reportNotice.getReport().getReportCategory().getCategoryName();
        this.reportedReviewResDto = reportedReviewResDto;
        this.isPossibleObjection = !reportNotice.getDetailReportNotice().getStopPostDateTime().plusDays(30).isBefore(LocalDateTime.now());
    }
}
