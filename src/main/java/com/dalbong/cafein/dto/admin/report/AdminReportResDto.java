package com.dalbong.cafein.dto.admin.report;

import com.dalbong.cafein.domain.report.ReportStatus;
import com.dalbong.cafein.domain.report.report.Report;
import com.dalbong.cafein.dto.admin.reportHistory.AdminReportHistoryResDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminReportResDto {

    private Long reportId;

    private Long reviewId;

    private Boolean isPostOfReview;

    private Long toMemberId;

    private String toMemberNickname;

    private Long fromMemberId;

    private String fromMemberNickname;

    private Long reportCategoryId;

    private String categoryName;

    private String content;

    private ReportStatus currentReportStatus;

    private List<AdminReportHistoryResDto> reportHistoryResDtoList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime modDateTime;

    private Long memoId;

    public AdminReportResDto(Report report, List<AdminReportHistoryResDto> reportHistoryResDtoList, Long memoId){
        this.reportId = report.getReportId();
        this.reviewId = report.getReview().getReviewId();
        this.isPostOfReview = report.getReview().getIsPost();
        this.toMemberId = report.getToMember().getMemberId();
        this.toMemberNickname = report.getToMember().getNickname();
        this.fromMemberId = report.getFromMember().getMemberId();
        this.fromMemberNickname = report.getFromMember().getNickname();
        this.reportCategoryId = report.getReportCategory() != null ? report.getReportCategory().getReportCategoryId() : null;
        this.categoryName = report.getReportCategory() != null ? report.getReportCategory().getCategoryName() : null;
        this.content = report.getContent() != null ? report.getContent() : null;
        this.currentReportStatus = report.getReportStatus();
        this.reportHistoryResDtoList = reportHistoryResDtoList;
        this.regDateTime = report.getRegDateTime();
        this.modDateTime = report.getModDateTime();
        this.memoId = memoId;
    }

    public AdminReportResDto(Report report, List<AdminReportHistoryResDto> reportHistoryResDtoList){
        this(report, reportHistoryResDtoList,null);
    }

}
