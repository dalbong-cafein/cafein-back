package com.dalbong.cafein.dataSet;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NaverSearchApi {

    private final StoreRepository storeRepository;

    public void createStore(Map<String, Object> searchData){

        List<NaverStoreDto> naverStoreDtoList = createNaverDto(searchData);

        List<Store> storeList = naverStoreDtoList.stream().map(dto -> {
            return Store.builder()
                    .storeName(dto.getStoreName())
                    .website(dto.getLink())
                    .phone(dto.getTelephone())
                    .address(dto.getAddress())
                    .mapX(dto.getMapX())
                    .mapY(dto.getMapY())
                    .build();
        }).collect(Collectors.toList());

        System.out.println("생성된 store 출력--------------------");
        if(!storeList.isEmpty()){
            for(Store store : storeList){
                System.out.println(store);
                Optional<Store> result = storeRepository.findByStoreName(store.getStoreName());
                if (result.isEmpty()){
                    storeRepository.save(store);
                }
            }
        }

    }


    /**
     * NaverDto 생성
     */
    public List<NaverStoreDto> createNaverDto(Map<String, Object> searchData){

        List<Map<String,Object>> arrays =  (List<Map<String,Object>>) searchData.get("items");

        List<NaverStoreDto> naverStoreDtoList = arrays.stream().map(attributes ->
                new NaverStoreDto(attributes)).collect(Collectors.toList());

        System.out.println("생성된 naverStoreDtoList 출력--------------------");
        if(!naverStoreDtoList.isEmpty()){
            for(NaverStoreDto naverStoreDto : naverStoreDtoList){
                System.out.println(naverStoreDto);
            }
        }

        return naverStoreDtoList;
    }

}
