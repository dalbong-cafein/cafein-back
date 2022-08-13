package com.dalbong.cafein.dataSet;

import com.dalbong.cafein.domain.member.Member;
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
public class NaverSearchService {

    private final StoreRepository storeRepository;

    public List<Store> createStore(Map<String, Object> searchData, Long principalId){

        List<NaverStoreDto> naverStoreDtoList = createNaverDto(searchData);

        List<Store> storeList = naverStoreDtoList.stream().map(dto -> {
            return Store.builder()
                    .storeName(dto.getStoreName())
                    .website(dto.getLink())
                    .regMember(Member.builder().memberId(principalId).build())
                    .modMember(Member.builder().memberId(principalId).build())
                    .phone(dto.getTelephone())
                    .address(dto.getAddress())
                    .build();
        }).collect(Collectors.toList());

        System.out.println("생성된 store 출력--------------------");

        List<Store> saveStoreList = new ArrayList<>();
        if(!storeList.isEmpty()){
            for(Store store : storeList){
                System.out.println(store);
                Optional<Store> result = storeRepository.findByStoreName(store.getStoreName());
                if (result.isEmpty()){
                    saveStoreList.add(store);
                    storeRepository.save(store);
                }
            }
        }
    return saveStoreList;
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


        String[] pattern = {"서대문구","마포구","성북구","동대문구","종로구","강남구"};
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
