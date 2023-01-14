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
@ToString
@Entity
public class DetailReportNotice {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailReportNoticeId;

    private LocalDateTime reportExpiredDateTime;

    private LocalDateTime stopPostDateTime;
}
