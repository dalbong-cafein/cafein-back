package com.dalbong.cafein.service.congestion;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.dalbong.cafein.dto.congestion.CongestionRegDto;

public interface CongestionService {

    Congestion register(CongestionRegDto congestionRegDto, Long principalId);


}
