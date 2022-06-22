package com.dalbong.cafein.service.congestion;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.dalbong.cafein.dto.congestion.CongestionListResDto;
import com.dalbong.cafein.dto.congestion.CongestionRegDto;
import com.dalbong.cafein.dto.congestion.CongestionResDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CongestionService {

    Congestion register(CongestionRegDto congestionRegDto, Long principalId);

    void remove(Long congestionId);

    CongestionListResDto<List<CongestionResDto>> getCongestionList(Long storeId, Integer minusDays);


}
