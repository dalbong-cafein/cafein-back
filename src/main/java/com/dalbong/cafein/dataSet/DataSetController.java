package com.dalbong.cafein.dataSet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class DataSetController {

    private final ObjectMapper objectMapper;
    private final NaverSearchService naverSearchService;
    private final GoogleSearchService googleSearchService;
    private final KakaoService kakaoService;
    private final RestTemplate rt;

    @Value("${dataSet.naver.clientId}")
    private String clientId;
    @Value("${dataSet.naver.clientSecret}")
    private String secretId;
    @Value("${dataSet.google.apiKey}")
    private String googleApiKey;
    @Value("${dataSet.kakao.apiKey}")
    private String kakaoApiKey;

    @PostMapping("/data/naver-search")
    public String naverSearch(@RequestParam("keyword") String keyword) throws JsonProcessingException {
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

        naverSearchService.createStore(searchData);


        return "검색 성공";
    }

    @PostMapping("/data/google-search")
    public String googleSearch(@RequestParam("keyword") String keyword) throws IOException {



        Map<String,Object> searchPlace =  rt.getForObject("https://maps.googleapis.com/maps/api/place/textsearch/json?query="
                        + keyword + "&key="+googleApiKey,
                Map.class);


        googleSearchService.placeSearch(searchPlace);


        //System.out.println(searchPlace);

        return "검색 성공";
    }

    @PostMapping("/data/kakao-search")
    public String kakaoSearch(@RequestParam("keyword") String keyword) throws JsonProcessingException {

        //HttpHeader 오브젝트 생성 (엔티티) - 헤더, 바디
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization","KakaoAK "+kakaoApiKey);

        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String,String>> kakaoSearchRequest =
                new HttpEntity<>(null, headers);


        //Http요청하기 - Post방식으로 -그리고 response 변수의 응답 받음.
        ResponseEntity<String> response = rt.exchange(
                "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + keyword +"&size=15",
                HttpMethod.GET,
                kakaoSearchRequest,
                String.class
        );

        Map<String,Object> searchData = objectMapper.readValue(response.getBody(),Map.class);

        kakaoService.searchPlace(searchData);


        return "검색 성공";
    }
}
