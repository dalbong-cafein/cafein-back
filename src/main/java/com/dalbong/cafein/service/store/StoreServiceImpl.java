package com.dalbong.cafein.service.store;

import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.businessHours.BusinessHoursRepository;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.store.StoreRegDto;
import com.dalbong.cafein.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Transactional
@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService{

    private final StoreRepository storeRepository;
    private final BusinessHoursRepository businessHoursRepository;
    private final ImageService imageService;

    /**
     * 관리자단 카페 등록
     */
    @Transactional
    @Override
    public Store registerByAdmin(StoreRegDto storeRegDto) throws IOException {

        //businessHours 엔티티 저장
        BusinessHours businessHours = storeRegDto.toBusinessHoursEntity();
        businessHoursRepository.save(businessHours);

        //store entity 저장
        Store store = storeRegDto.toEntity();
        store.changeIsApproval(); //관리자가 등록시 바로 승인
        store.changeBusinessHours(businessHours);

        storeRepository.save(store);

        imageService.saveStoreImage(store, storeRegDto.getImageFiles());

        return store;
    }

    /**
     * 카페 승인 여부 수정
     */
    @Transactional
    @Override
    public void modifyIsApproval(Long storeId) {

        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new IllegalArgumentException("존재하는 않는 가게입니다."));

        store.changeIsApproval();
    }




}

