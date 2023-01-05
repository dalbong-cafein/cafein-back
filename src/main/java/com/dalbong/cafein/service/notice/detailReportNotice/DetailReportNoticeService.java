package com.dalbong.cafein.service.notice.detailReportNotice;

import com.dalbong.cafein.domain.notice.Notice;
import com.dalbong.cafein.domain.notice.detailReportNotice.DetailReportNotice;
import com.dalbong.cafein.domain.notice.detailReportNotice.DetailReportNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional
@RequiredArgsConstructor
@Service
public class DetailReportNoticeService {

    private final DetailReportNoticeRepository detailReportNoticeRepository;

    /**
     * 상세 신고 알림 저장
     */
    @Transactional
    public DetailReportNotice register(Notice notice, LocalDateTime reportExpiredDateTime, LocalDateTime stopPostDateTime){

        DetailReportNotice detailReportNotice = DetailReportNotice.builder()
                .notice(notice)
                .reportExpiredDateTime(reportExpiredDateTime)
                .stopPostDateTime(stopPostDateTime)
                .build();

        return detailReportNoticeRepository.save(detailReportNotice);
    }

}
