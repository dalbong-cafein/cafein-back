package com.dalbong.cafein.dataSet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RequiredArgsConstructor
@RestController
public class DataSetController {

    private final ObjectMapper objectMapper;
    private final NaverSearchApi naverSearchApi;
    private final RestTemplate rt;
    private String clientId = "dOXazpqK7gPsCGdr9Bou";
    private String secretId = "onJyZriJJg";

    @PostMapping("/data/naver-search")
    public String search(@RequestParam("keyword") String keyword) throws JsonProcessingException {
        //POST 방식으로 key=value 데이터를 요청 (카카오쪽으로)
        RestTemplate rt = new RestTemplate();

        //HttpHeader 오브젝트 생성 (엔티티) - 헤더, 바디
        HttpHeaders headers = new HttpHeaders();

        headers.add("X-Naver-Client-Id",clientId);
        headers.add("X-Naver-Client-Secret",secretId);

        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String,String>> naverSearchRequest =
                new HttpEntity<>(null, headers);


        //Http요청하기 - Post방식으로 -그리고 response 변수의 응답 받음.
        ResponseEntity<String> response = rt.exchange(
                "https://openapi.naver.com/v1/search/local.json?query=" + keyword +"&display=20",
                HttpMethod.GET,
                naverSearchRequest,
                String.class
        );

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());

        Map<String,Object> searchData = objectMapper.readValue(response.getBody(),Map.class);

        System.out.println("========================");
        System.out.println(searchData);

        naverSearchApi.createStore(searchData);


        return "검색 성공";
    }
}
