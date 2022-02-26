package com.dalbong.cafein.service;

import com.dalbong.cafein.domain.heart.Heart;
import com.dalbong.cafein.domain.heart.HeartRepository;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class HeartServiceImpl implements HeartService{

    private HeartRepository heartRepository;
    private StoreRepository storeRepository;

    /**
     * 관심 추가
     */
    @Transactional
    @Override
    public Heart heart(Long principalId, Long storeId) {

        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new IllegalArgumentException("존재하지 않는 가게입니다."));

        Heart heart = Heart.builder()
                .member(Member.builder().memberId(principalId).build())
                .build();

        heart.setStore(store);

        return heartRepository.save(heart);
    }

    /**
     * 관심 취소
     */
    @Transactional
    @Override
    public void cancelHeart(Long principalId, Long storeId) {

        heartRepository.deleteHeart(principalId,storeId);
    }
}
