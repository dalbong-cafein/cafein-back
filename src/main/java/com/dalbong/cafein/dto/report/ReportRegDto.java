package com.dalbong.cafein.dto.report;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.report.Report;
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

    private Long reportCategoryId;

    private String content;


    public Report toEntity(){

        if(this.reportCategoryId != null){
            return Report.builder()
                    .review(Review.builder().reviewId(this.reviewId).build())
                    .reportCategory(ReportCategory.builder().reportCategoryId(reportCategoryId).build())
                    .build();
        }else{ //기타
            return Report.builder()
                    .review(Review.builder().reviewId(this.reviewId).build())
                    .content(this.content)
                    .build();
        }
    }
}
