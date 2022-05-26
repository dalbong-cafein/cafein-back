package com.dalbong.cafein.dto.report;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.report.Report;
import com.dalbong.cafein.domain.reportCategory.ReportCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReportRegDto {

    private Long toMemberId;

    private Long reportCategoryId;

    private String content;


    public Report toEntity(){

        return Report.builder()
                .toMember(Member.builder().memberId(this.toMemberId).build())
                .reportCategory(ReportCategory.builder().reportCategoryId(this.reportCategoryId).build())
                .content(this.content)
                .build();
    }
}
