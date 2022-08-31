package com.dalbong.cafein.dataSet;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dataSet.review.ExcelReviewDataService;
import com.dalbong.cafein.dataSet.store.*;
import com.dalbong.cafein.dataSet.store.google.GoogleSearchService;
import com.dalbong.cafein.dataSet.store.kakao.KakaoService;
import com.dalbong.cafein.dataSet.store.naver.NaverCloudService;
import com.dalbong.cafein.dataSet.store.naver.NaverSearchService;
import com.dalbong.cafein.dataSet.subwayStation.SubwayStationRegDto;
import com.dalbong.cafein.dataSet.subwayStation.SubwayStationService;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.CMRespDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
    private final NaverCloudService naverCloudService;
    private final StoreRepository storeRepository;
    private final SubwayStationService subwayStationService;
    private final ExcelReviewDataService excelReviewDataService;
    private final ExcelStoreDataService excelStoreDataService;

    @Value("${dataSet.naver.clientId}")
    private String clientId;
    @Value("${dataSet.naver.clientSecret}")
    private String secretId;
    @Value("${dataSet.google.apiKey}")
    private String googleApiKey;
    @Value("${dataSet.kakao.apiKey}")
    private String kakaoApiKey;


    @GetMapping("/data/naver-search")
    public ResponseEntity<?> naverSearch(@RequestParam("keyword") String keyword,
                              @AuthenticationPrincipal PrincipalDetails principalDetails) throws JsonProcessingException {
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

        List<Store> saveStoreList = naverSearchService.createStore(searchData, principalDetails.getMember().getMemberId());

        List<SaveStoreResDto> saveStoreResDtoList = new ArrayList<>();
        if(saveStoreList != null){
            for (Store store : saveStoreList){
                saveStoreResDtoList.add(new SaveStoreResDto(store.getStoreName()));
            }
        }

        return new ResponseEntity<>(new CMRespDto<>(1, "저장된 카페 리스트",saveStoreResDtoList), HttpStatus.OK);
    }

    @PostMapping("/data/naver-mapping")
    public ResponseEntity<?> naverMapping(@AuthenticationPrincipal PrincipalDetails principalDetails) throws JsonProcessingException {

        //POST 방식으로 key=value 데이터를 요청
        RestTemplate rt = new RestTemplate();

        //HttpHeader 오브젝트 생성 (엔티티) - 헤더, 바디
        HttpHeaders headers = new HttpHeaders();

        headers.add("X-Naver-Client-Id",clientId);
        headers.add("X-Naver-Client-Secret",secretId);

        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String,String>> naverSearchRequest =
                new HttpEntity<>(null, headers);


        String[] aList = {"스타벅스", "투썸플레이스", "할리스", "이디야 커피", "탐앤탐스",
                "커피빈", "빽다방", "메가커피", "더벤티", "컴포즈커피", "매머드", "요거프레소",
                "엔젤리너스", "커피니", "카페", "커피"};

        String[] bList = {"서대문구","마포구","성북구","동대문구","종로구","강남구"};

        List<SaveStoreResDto> saveStoreResDtoList = new ArrayList<>();

        for (String a : aList){
            for(String b : bList){

                String keyword = a + " " + b;

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


                List<Store> saveStoreList = naverSearchService.createStore(searchData, principalDetails.getMember().getMemberId());


                if(saveStoreList != null){
                    for (Store store : saveStoreList){
                        saveStoreResDtoList.add(new SaveStoreResDto(store.getStoreName()));
                    }
                }
            }
        }

        return new ResponseEntity<>(new CMRespDto<>(1, "저장된 카페 리스트",saveStoreResDtoList), HttpStatus.OK);
    }

    @GetMapping("/data/google-search")
    public String googleSearch(@RequestParam("keyword") String keyword) throws IOException {



        Map<String,Object> searchPlace =  rt.getForObject("https://maps.googleapis.com/maps/api/place/textsearch/json?query="
                        + keyword + "&key="+googleApiKey,
                Map.class);

        googleSearchService.placeSearch(searchPlace);


        //System.out.println(searchPlace);

        return "검색 성공";
    }

    @GetMapping("/data/google-mapping")
    public String googleMapping(@RequestParam("keyword") String sggNm) throws IOException {

        List<Store> sggStoreList = storeRepository.findByAddress_SggNm(sggNm);

        for (Store store : sggStoreList){
            Map<String,Object> searchPlace =  rt.getForObject("https://maps.googleapis.com/maps/api/place/textsearch/json?query="
                            + store.getStoreName() + "&key="+googleApiKey,
                    Map.class);

            googleSearchService.placeSearch(searchPlace);
        }

        //System.out.println(searchPlace);

        return "검색 성공";
    }

    @GetMapping("/data/kakao-search")
    public ResponseEntity<?> kakaoSearch(@RequestParam("keyword") String keyword) throws JsonProcessingException {

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

        List<Store> updateStoreList = kakaoService.searchPlace(searchData);

        List<SaveStoreResDto> updateStoreResDtoList = new ArrayList<>();
        if(updateStoreList != null){
            for (Store store : updateStoreList){
                updateStoreResDtoList.add(new SaveStoreResDto(store.getStoreName()));
            }
        }

        return new ResponseEntity<>(new CMRespDto<>(1, "카카오로부터 추가 데이터를 얻은 카페 리스트",updateStoreResDtoList), HttpStatus.OK);
    }

    @GetMapping("/data/kakao-mapping")
    public ResponseEntity<?> kakaoMapping(@RequestParam("keyword") String sggNm) throws JsonProcessingException {

        List<Store> sggStoreList = storeRepository.findByAddress_SggNm(sggNm);


        for(Store store : sggStoreList){
            //HttpHeader 오브젝트 생성 (엔티티) - 헤더, 바디
            HttpHeaders headers = new HttpHeaders();

            headers.add("Authorization","KakaoAK "+kakaoApiKey);

            //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
            HttpEntity<MultiValueMap<String,String>> kakaoSearchRequest =
                    new HttpEntity<>(null, headers);


            //Http요청하기 - Post방식으로 -그리고 response 변수의 응답 받음.
            ResponseEntity<String> response = rt.exchange(
                    "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + store.getStoreName() +"&size=15",
                    HttpMethod.GET,
                    kakaoSearchRequest,
                    String.class
            );

            Map<String,Object> searchData = objectMapper.readValue(response.getBody(),Map.class);

            List<Store> updateStoreList = kakaoService.searchPlace(searchData);

//            List<SaveStoreResDto> updateStoreResDtoList = new ArrayList<>();
//            if(updateStoreList != null){
//                for (Store store : updateStoreList){
//                    updateStoreResDtoList.add(new SaveStoreResDto(store.getStoreName()));
//                }
//            }
        }



        return new ResponseEntity<>(new CMRespDto<>(1, "카카오로부터 추가 데이터를 얻은 카페 리스트",null), HttpStatus.OK);
    }

    @PatchMapping("/data/naver-cloud-xy")
    public String saveLatAndLng() throws JsonProcessingException {

        naverCloudService.saveLatAndLng();

        return "위도 경도 저장 성공";
    }

    @PostMapping("/data/subwayStations")
    public String saveSubwayStation(@RequestBody SubwayStationRegDto subwayStationRegDto){

        subwayStationService.save(subwayStationRegDto.getData());

        return "지하철역 데이터 저장 성공";
    }

    @PatchMapping("/data/subwayStations/isUse")
    public String modifyIsUser() throws JsonProcessingException {

        naverCloudService.modifyStationIsUse();

        return "지하철역 데이터 사용여부 수정 성공";
    }

    @PostMapping("/data/nearStoreToSubwayStations")
    public String saveDistance(){

        subwayStationService.saveNearStoreToSubwayStation();

        return "역과 가까운 카페 데이터 저장 성공";
    }

    @PostMapping("/data/reviews")
    public String registerReview(MultipartFile excelFile,
                                 @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException, InterruptedException {

        excelReviewDataService.register(excelFile, principalDetails.getMember());

        return "엑셀 리뷰 데이터 추가 성공";
    }

    @PatchMapping("/data/stores/phone")
    public String updatePhoneOfStore(MultipartFile excelFile,
                                     @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        excelStoreDataService.updatePhoneData(excelFile, principalDetails.getMember());

        return "엑셀 카페 전화번호 데이터 추가 성공";
    }
}
