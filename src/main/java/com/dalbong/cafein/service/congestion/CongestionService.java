package com.dalbong.cafein.service.congestion;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.dalbong.cafein.dto.PossibleRegistrationResDto;
import com.dalbong.cafein.dto.admin.congestion.AdminCongestionListResDto;
import com.dalbong.cafein.dto.admin.congestion.AdminCongestionResDto;
import com.dalbong.cafein.dto.congestion.CongestionListResDto;
import com.dalbong.cafein.dto.congestion.CongestionRegDto;
import com.dalbong.cafein.dto.congestion.CongestionResDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CongestionService {

    PossibleRegistrationResDto checkPossibleRegistration(Long storeId, Long principalId);

    Congestion register(CongestionRegDto congestionRegDto, Long principalId);

    void remove(Long congestionId);

    CongestionListResDto<List<CongestionResDto>> getCongestionList(Long storeId, Integer minusDays);

    AdminCongestionListResDto<List<AdminCongestionResDto>> getCongestionListOfAdmin(Long storeId, Integer minusDays);

}
