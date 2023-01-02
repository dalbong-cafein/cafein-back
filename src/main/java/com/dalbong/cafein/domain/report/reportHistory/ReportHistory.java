package com.dalbong.cafein.domain.report.reportHistory;

import com.dalbong.cafein.domain.BaseEntity;
import com.dalbong.cafein.domain.report.report.Report;
import com.dalbong.cafein.domain.report.ReportStatus;
import lombok.*;

import javax.persistence.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
public class ReportHistory extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportHistoryId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

}
