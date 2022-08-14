package com.dalbong.cafein.dataSet.review;

import com.dalbong.cafein.dataSet.store.NaverSearchService;
import com.dalbong.cafein.dataSet.util.excel.ExcelRead;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.review.ReviewRepository;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ExcelReviewDataService {

    @Value("${dataSet.naver.clientId}")
    private String clientId;
    @Value("${dataSet.naver.clientSecret}")
    private String secretId;

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final ExcelRead excelRead;
    private final ObjectMapper objectMapper;
    private final NaverSearchService naverSearchService;

    @Transactional
    public void register(MultipartFile excelFile, Member member) throws IOException {

        //파일 읽기
        List<ExcelReviewDataDto> excelReviewDataDtoList = excelRead.read(excelFile);

        //데이터 매핑
        for (ExcelReviewDataDto dataDto : excelReviewDataDtoList){
            Optional<Store> result = storeRepository.findByStoreName(dataDto.getStoreName());

            Review review = null;

            //기존 DB에 저장되어 있는 카페일 경우
            if(result.isPresent()){
                Store store = result.get();
                review = dataDto.toReview(store, member);
            }
            //새로운 카페 데이터일 경우
            else{
                //naver 지역 api 호출
                Map<String, Object> searchData = requestNaverSearchApi(dataDto.getStoreName());

                //카페 데이터 저장
                naverSearchService.createStore(searchData, member.getMemberId());

                Optional<Store> repeatResult
                        = storeRepository.findByStoreName(dataDto.getStoreName());

                if(repeatResult.isPresent()){
                    review = dataDto.toReview(repeatResult.get(), member);
                }else{
                    System.out.println(dataDto.getStoreName() + "- 새로운 카페 데이터를 저장하지 못했습니다.");
                }
            }


            //리뷰 데이터 저장
            if(review != null){
                reviewRepository.save(review);
            }
        }
    }

    private Map<String,Object> requestNaverSearchApi(String keyword) throws JsonProcessingException {

//POST 방식으로 key=value 데이터를 요청
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

        return searchData;
    }


}
