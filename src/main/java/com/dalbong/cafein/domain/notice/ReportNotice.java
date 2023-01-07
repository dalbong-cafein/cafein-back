package com.dalbong.cafein.domain.notice;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.notice.detailReportNotice.DetailReportNotice;
import com.dalbong.cafein.domain.report.report.Report;
import edu.emory.mathcs.backport.java.util.Arrays;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"report","detailReportNoticeList"})
@DiscriminatorValue("report")
@Entity
public class ReportNotice extends Notice{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private Report report;

    @OneToMany(mappedBy = "reportNotice", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<DetailReportNotice> detailReportNoticeList;

    public ReportNotice(Report report, Member toMember, String content){
        super(toMember, content);
        this.report = report;
        detailReportNoticeList = new ArrayList<>();
    }

}
