package com.dalbong.cafein.domain.notice;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.report.report.Report;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@ToString(exclude = {"report"})
@DiscriminatorValue("report")
@Entity
public class ReportNotice extends Notice{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private Report report;

    public ReportNotice(Report report, Member toMember, String content){
        super(toMember, content);
        this.report = report;
    }

}
