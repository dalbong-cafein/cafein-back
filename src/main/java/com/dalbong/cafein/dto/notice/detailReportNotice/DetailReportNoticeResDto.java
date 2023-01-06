package com.dalbong.cafein.dto.notice.detailReportNotice;

import com.dalbong.cafein.domain.notice.detailReportNotice.DetailReportNotice;
import com.dalbong.cafein.dto.reportCategory.ReportCategoryResDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DetailReportNoticeResDto {

    private Long detailReportNoticeId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime reportExpiredDateTime;

    private String reportCategoryName;

    private ReportedReviewResDto reportedReviewResDto;

    private Boolean isPossibleObjection;

    public DetailReportNoticeResDto(DetailReportNotice detailReportNotice, ReportedReviewResDto reportedReviewResDto){

        this.detailReportNoticeId = detailReportNotice.getDetailReportNoticeId();
        this.reportExpiredDateTime = detailReportNotice.getReportExpiredDateTime() != null ?
                detailReportNotice.getReportExpiredDateTime().minusDays(1).toLocalDate().atTime(23,59,59) : null;
        this.reportCategoryName = detailReportNotice.getReportNotice().getReport().getReportCategory().getCategoryName();
        this.reportedReviewResDto = reportedReviewResDto;
        this.isPossibleObjection = !detailReportNotice.getStopPostDateTime().plusDays(30).isBefore(LocalDateTime.now());
    }
}
