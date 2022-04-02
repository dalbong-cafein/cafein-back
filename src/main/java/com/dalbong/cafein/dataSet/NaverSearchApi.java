package com.dalbong.cafein.dataSet;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.PatternMatchUtils;

import java.util.ArrayList;
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

        List<NaverStoreDto> naverStoreDtoList = new ArrayList<>();

        //카페만 naverStoreDto 생성
        for(Map<String,Object> attributes : arrays){
            String category = (String) attributes.get("category");
            if (category.contains("카페")){
                naverStoreDtoList.add(new NaverStoreDto(attributes));
            }
        }


        String[] pattern = {"서대문구","마포구","노원구","동대문구","종로구","강남구"};
        String[] ignorePattern = {"*배스킨라빈스*"};
        List<NaverStoreDto> newNaverStoreDtoList = new ArrayList<>();
        for (NaverStoreDto dto : naverStoreDtoList){
            if(PatternMatchUtils.simpleMatch(pattern,dto.getAddress().getSggNm())
                    && !PatternMatchUtils.simpleMatch(ignorePattern, dto.getStoreName())){
                newNaverStoreDtoList.add(dto);
            }
        }

        System.out.println("생성된 naverStoreDtoList 출력--------------------");
        if(!newNaverStoreDtoList.isEmpty()){
            for(NaverStoreDto naverStoreDto : naverStoreDtoList){
                System.out.println(naverStoreDto);
            }
        }

        return newNaverStoreDtoList;
    }

}
