package com.dalbong.cafein.dataSet;

import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class GoogleSearchService {

    @Value("${dataSet.google.apiKey}")
    private String googleApiKey;

    private final RestTemplate rt;
    private final StoreRepository storeRepository;
    private ImageService imageService;


    public void placeSearch(Map<String,Object> searchData){

        List<Map<String,Object>> searchPlaceData =  (List<Map<String,Object>>) searchData.get("results");

        List<String> placeIdList = new ArrayList<>();

        for (Map<String,Object> arr : searchPlaceData){
            placeIdList.add((String) arr.get("place_id"));
        }

        List<GoogleStoreDto> googleStoreDtoList = detailsPlaceData(placeIdList);

        for (GoogleStoreDto dto : googleStoreDtoList){
            System.out.println(dto);
        }


    }

    /**
     * Google place Details API
     */
    private List<GoogleStoreDto> detailsPlaceData(List<String> placeIdList) {

        List<GoogleStoreDto> googleStoreDtoList = new ArrayList<>();

        for (String placeId : placeIdList){
            Map<String,Object> detailPlaceData = rt.getForObject("https://maps.googleapis.com/maps/api/place/details/json?place_id="+placeId
                    + "&key=" + googleApiKey, Map.class);

            System.out.println(detailPlaceData);

            Map<String,Object> result = (Map<String,Object>)detailPlaceData.get("result");

            //storeName
            String name = (String) result.get("name");

            //phone
            String phone = (String) result.get("formatted_phone_number");

            System.out.println(name);
            System.out.println(phone);

            //lat, lng
            Map<String, Object> geometry = (Map<String, Object>) result.get("geometry");
            Map<String, Object> viewport = (Map<String, Object>) geometry.get("viewport");
            Map<String, Double> southwest = (Map<String, Double>) viewport.get("southwest");

            Double lngX = southwest.get("lng");
            Double latY = southwest.get("lat");

            List<Map<String,Object>> daysOpening = null;
            //daysOpening
            Map<String, Object> openingHours = (Map<String, Object>) result.get("opening_hours");

            try {
                daysOpening =(List<Map<String,Object>>)openingHours.get("periods");
            }catch (NullPointerException e){
                System.out.println(name + "운영시간 데이터가 없습니다.");
            }

            //photoReference
            List<Map<String, Object>> photos = (List<Map<String, Object>>) result.get("photos");

            List<String> photoReferenceList = new ArrayList<>();
            if (photos != null){
                for (Map<String, Object> arr : photos){
                    try {
                        photoReferenceList.add((String)arr.get("photo_reference"));
                    }catch (NullPointerException e){
                        System.out.println(name + "해당 이미지 데이터가 없습니다.");
                    }
                }
            }else{
                System.out.println(name + "카페 이미지 데이터 자체가 없습니다.");
            }

            googleStoreDtoList.add(new GoogleStoreDto(name,phone, lngX, latY, daysOpening, photoReferenceList));
        }

        return googleStoreDtoList;
    }

    private void photosPlaceData(){

    }

}
