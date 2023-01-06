package com.dalbong.cafein.domain.notice.detailReportNotice;

import com.dalbong.cafein.domain.notice.Notice;
import com.dalbong.cafein.domain.notice.ReportNotice;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "report_notice_id")
    private ReportNotice reportNotice;

    private LocalDateTime reportExpiredDateTime;

    private LocalDateTime stopPostDateTime;
}
