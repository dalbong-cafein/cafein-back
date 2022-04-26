package com.dalbong.cafein.service.congestion;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.dalbong.cafein.domain.congestion.CongestionRepository;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.congestion.CongestionRegDto;
import com.dalbong.cafein.handler.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CongestionServiceImpl implements CongestionService{

    private final CongestionRepository congestionRepository;
    private final StoreRepository storeRepository;

    /**
     * 혼잡도 등록
     */
    @Transactional
    @Override
    public Congestion register(CongestionRegDto congestionRegDto, Long principalId) {
        Store store = storeRepository.findById(congestionRegDto.getStoreId()).orElseThrow(() ->
                new CustomException("존재하지 않는 가게입니다."));

        //같은 카페의 혼잡도 2시간이내 등록 여부 체크
        boolean isExist = congestionRepository.existWithinTime(congestionRegDto.getStoreId(), principalId);

        if(isExist){
            throw new CustomException("혼잡도 등록 2시간안에는 새로운 등록을 할 수 없습니다.");
        }

        Congestion congestion = congestionRegDto.toEntity(store, principalId);
        congestionRepository.save(congestion);

        //TODO 스탬프 증정

        return congestion;
    }
}
