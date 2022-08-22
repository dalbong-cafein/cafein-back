package com.dalbong.cafein.web.service;

import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.web.domain.contents.ContentsRepository;
import com.dalbong.cafein.web.domain.contents.ContentsType;
import com.dalbong.cafein.web.dto.StoreResDtoOfWeb;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ContentsService {

    private final StoreRepository storeRepository;

    /**
     * 지역별 컨텐츠 카페 추천 리스트 조회
     */
    public StoreResDtoOfWeb getContentsStoreList(String sgg, ContentsType type){

        

        return null;
    }


}
