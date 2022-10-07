package com.dalbong.cafein.domain.memo;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.report.Report;
import com.dalbong.cafein.domain.review.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@ToString(exclude = {"report"})
@DiscriminatorValue("report")
@Entity
public class ReportMemo extends Memo{

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private Report report;

    public ReportMemo(Report report, Member writer, String content){
        super(writer, content);
        this.report = report;
    }
}
