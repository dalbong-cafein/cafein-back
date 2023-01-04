package com.dalbong.cafein.domain.notice.detailReportNotice;

import com.dalbong.cafein.domain.notice.Notice;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"report"})
@Entity
public class DetailReportNotice {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailReportNoticeId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Notice notice;

    private LocalDate reportExpiredDate;

    private LocalDateTime stopPostDateTime;


}
