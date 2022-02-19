package com.dalbong.cafein.service;

import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.businessHours.BusinessHoursRepository;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.store.StoreRegDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService{

    private final StoreRepository storeRepository;
    private final BusinessHoursRepository businessHoursRepository;

    /**
     * 카페 등록
     */
    @Transactional
    @Override
    public Store register(StoreRegDto storeRegDto, Long principalId) {

        //store entity 저장
        Store store = storeRegDto.toEntity(principalId);
        storeRepository.save(store);

        //businessHours 엔티티 저장
        BusinessHours businessHours = storeRegDto.getBusinessHoursRegDto().toEntity(store.getStoreId());
        businessHoursRepository.save(businessHours);

        //TODO 이미지 처리

        return store;
    }

    /**
     * 카페 승인 수정
     */
    @Transactional
    @Override
    public void modifyIsApproval(Long storeId) {

        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new IllegalArgumentException("존재하는 않는 가게입니다."));

        store.changeIsApproval();
    }




}

