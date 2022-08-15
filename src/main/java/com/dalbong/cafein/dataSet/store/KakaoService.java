package com.dalbong.cafein.dataSet.store;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class KakaoService {

    private final StoreRepository storeRepository;

    @Transactional
    public List<Store> searchPlace(Map<String,Object> searchData){

        List<Store> findStoreList = storeRepository.findAll();

        List<Map<String, Object>> result = (List<Map<String, Object>>) searchData.get("documents");

        List<KakaoStoreDto> kakaoStoreDtoList = new ArrayList<>();

        //KakoStoreDto 생성
        for (Map<String,Object> arr : result){
            String category = (String) arr.get("category_group_name");
            if (category.contains("카페")){
                kakaoStoreDtoList.add(new KakaoStoreDto(arr));
            }
        }

        //갱신되는 StoreList
        List<Store> updateStoreList = new ArrayList<>();

        //기존 store 데이터에 카카오 지역 api 데이터 추가
        for (KakaoStoreDto dto : kakaoStoreDtoList){
            System.out.println(dto);
            for (Store store : findStoreList){
                if(dto.getAddress() == null){
                    break;
                }
                if(dto.getAddress().getSggNm().equals(store.getAddress().getSggNm())
                        && dto.getAddress().getRNm().equals(store.getAddress().getRNm())
                        && dto.getAddress().getRNum().equals(store.getAddress().getRNum())){

                    store.changeLatAndLng(dto.getX(), dto.getY());
                    store.changePhone(dto.getPhone());

                    updateStoreList.add(store);

                    System.out.println(store);
                    break;
                }
            }
        }
        return updateStoreList;
    }


}
