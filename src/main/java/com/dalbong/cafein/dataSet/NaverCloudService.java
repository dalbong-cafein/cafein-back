package com.dalbong.cafein.dataSet;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class NaverCloudService {

    @Value("${dataSet.naverCloud.clientId}")
    private String cloudClientId;
    @Value("${dataSet.naverCloud.clientSecret}")
    private String cloudSecretId;

    private final ObjectMapper objectMapper;
    private final StoreRepository storeRepository;

    @Transactional
    public void saveLatAndLng() throws JsonProcessingException {

        List<Store> storeList = storeRepository.findByLatYAndLngXIsNull();


        for(Store store : storeList){
            String fullAddress = store.getAddress().getFullAddress();
            NaverCloudDto naverCloudDto = getLatAndLngByNaverCloud(fullAddress);

            if(naverCloudDto != null){
                store.changeLatAndLng(naverCloudDto.getX(), naverCloudDto.getY());
            }
        }



    }

    private NaverCloudDto getLatAndLngByNaverCloud(String keyword) throws JsonProcessingException {

        //POST 방식으로 key=value 데이터를 요청
        RestTemplate rt = new RestTemplate();

        //HttpHeader 오브젝트 생성 (엔티티) - 헤더, 바디
        HttpHeaders headers = new HttpHeaders();

        headers.add("X-NCP-APIGW-API-KEY-ID",cloudClientId);
        headers.add("X-NCP-APIGW-API-KEY",cloudSecretId);

        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String,String>> naverSearchRequest =
                new HttpEntity<>(null, headers);


        //Http요청하기 - get방식으로 -그리고 response 변수의 응답 받음.
        ResponseEntity<String> response = rt.exchange(
                "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + keyword,
                HttpMethod.GET,
                naverSearchRequest,
                String.class
        );

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        Map<String,Object> searchData = objectMapper.readValue(response.getBody(),Map.class);

        System.out.println("========================");
        System.out.println(searchData);

        List<Map<String, Object>> addresses = (List<Map<String, Object>>) searchData.get("addresses");

        try {
            Map<String, Object> address = addresses.get(0);
            String roadAddress = (String) address.get("roadAddress");
            System.out.println(roadAddress);

            String x = (String) address.get("x");
            String y = (String) address.get("y");
            return new NaverCloudDto(Double.parseDouble(x), Double.parseDouble(y));

        }catch (Exception e){
            System.out.println("결과 값이 없습니다.");
        }

       return null;
    }

}
