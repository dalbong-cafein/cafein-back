package com.dalbong.cafein.domain.notice.detailReportNotice;

import com.dalbong.cafein.domain.notice.ReportNotice;
import com.dalbong.cafein.domain.store.Store;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"reportNotice"})
@Entity
public class DetailReportNotice {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailReportNoticeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_notice_id")
    private ReportNotice reportNotice;

    private LocalDateTime reportExpiredDateTime;

    private LocalDateTime stopPostDateTime;

    //연관관계 메서드
    public void setReportNotice(ReportNotice reportNotice){
        this.reportNotice = reportNotice;
        reportNotice.getDetailReportNoticeList().add(this);
    }
}
