package com.dalbong.cafein.service.congestion;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.dalbong.cafein.domain.congestion.CongestionRepository;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.congestion.CongestionListResDto;
import com.dalbong.cafein.dto.congestion.CongestionRegDto;
import com.dalbong.cafein.dto.congestion.CongestionResDto;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.service.sticker.StickerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class CongestionServiceImpl implements CongestionService{

    private final CongestionRepository congestionRepository;
    private final StoreRepository storeRepository;
    private final StickerService stickerService;

    /**
     * 혼잡도 등록
     */
    @Transactional
    @Override
    public Congestion register(CongestionRegDto congestionRegDto, Long principalId) {
        Store store = storeRepository.findById(congestionRegDto.getStoreId()).orElseThrow(() ->
                new CustomException("존재하지 않는 가게입니다."));

        Congestion congestion = congestionRegDto.toEntity(store, principalId);
        congestionRepository.save(congestion);

        return congestion;
    }

    /**
     * 혼잡도 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public CongestionListResDto<List<CongestionResDto>> getCongestionList(Long storeId, Integer minusDays) {

        List<Congestion> results = congestionRepository.getCongestionList(storeId, minusDays);

        List<CongestionResDto> congestionResDtoList =
                results.stream().map(c -> new CongestionResDto(c)).collect(Collectors.toList());

        return new CongestionListResDto<>(results.size(), congestionResDtoList);
    }
}
