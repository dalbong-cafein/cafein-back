package com.dalbong.cafein.dto.report;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.report.report.Report;
import com.dalbong.cafein.domain.reportCategory.ReportCategory;
import com.dalbong.cafein.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReportRegDto {

    @NotNull
    private Long reviewId;

    @NotNull
    private Long reportCategoryId;

    private String content;


    public Report toEntity(Review review, Member fromMember){

        return Report.builder()
                .review(review)
                .toMember(Member.builder().memberId(review.getMember().getMemberId()).build())
                .fromMember(fromMember)
                .reportCategory(ReportCategory.builder().reportCategoryId(reportCategoryId).build())
                .content(this.content)
                .build();

    }
}
