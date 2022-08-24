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
    public ResponseEntity<?> naverMapping(@AuthenticationPrincipal PrincipalDetails principalDetails) throws JsonProcessingException, InterruptedException {

        //POST 방식으로 key=value 데이터를 요청
        RestTemplate rt = new RestTemplate();

        //HttpHeader 오브젝트 생성 (엔티티) - 헤더, 바디
        HttpHeaders headers = new HttpHeaders();

        headers.add("X-Naver-Client-Id",clientId);
        headers.add("X-Naver-Client-Secret",secretId);

        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String,String>> naverSearchRequest =
                new HttpEntity<>(null, headers);

        List<Store> storeList = storeRepository.findAll();
        String[] aList = {"스벅", "투썸", "이디야", "탐탐" ,"컴포즈"};

        String[] bList = {
                "공덕동","아현동", "도화동", "용강동", "대흥동",
                "염리동", "신수동", "서강동", "서교동", "합정동", "망원1동", "망원2동", "연남동", "성산1동",
                "성단2동", "상암동", "충현동", "천연동", "북아현동", "신촌동", "연희동", "홍제 1동", "홍제 2동", "홍제 3동", "홍은 1동",
                "홍은 2동", "남가좌 1동", "남가좌 2동", "북가좌 1동", "북가좌 2동", "용신동", "제기동", "전농제1동",
                "전농제2동", "답십리제1동", "답십리제2동", "장안제1동", "장안제2동", "청량리동",
                "회기동", "휘경제1동", "휘경제2동", "이문제1동", "이문제2동", "성북동", "삼선동", "동선동", "돈암 1동",
                "돈암 2동", "안암동", "보문동", "정릉 1동", "정릉 2동", "정릉 3동", "정릉 4동", "종암동",
                "길음 1동", "길음 2동", "월곡 1동", "월곡 2동", "장위 1동", "장위 2동", "장위 3동",
                "석관동", "청운효자동", "사직동", "삼청동", "부암동", "평창동", "무악동", "교남동","가회동",
                "종로1가동", "종로2가동", "종로3가동", "종로4가동", "종로5가동", "종로5가동", "이화동",
                "혜화동", "창신1동", "창신2동", "창신3동", "숭인1동", "숭인2동", "신사동", "압구정동",
                "논현 1동", "논현 2동", "청담동", "삼성 1동", "삼성 2동", "대치 1동", "대치 2동", "대치 4동",
                "역삼 1동", "역삼 2동", "도곡 1동", "도곡 2동", "개포 1동", "개포 2동", "개포 4동", "일원본동",
                "일원 1동", "일원 2동", "수서동", "세곡동"
        };

        List<SaveStoreResDto> saveStoreResDtoList = new ArrayList<>();

        for (Store findStore : storeList){
            for(String b : bList){

                String keyword = findStore.getStoreName() + " " + b;
                System.out.println("--------------");
                System.out.println(keyword);
                Thread.sleep(50);

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
