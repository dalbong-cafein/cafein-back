package com.dalbong.cafein.dataSet;

import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.businessHours.BusinessHoursRepository;
import com.dalbong.cafein.domain.image.StoreImage;
import com.dalbong.cafein.domain.image.StoreImageRepository;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.s3.S3Uploader;
import com.dalbong.cafein.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class GoogleSearchService {

    @Value("${dataSet.google.apiKey}")
    private String googleApiKey;

    private final RestTemplate rt;
    private final StoreRepository storeRepository;
    private final BusinessHoursRepository businessHoursRepository;
    private final S3Uploader s3Uploader;
    private final StoreImageRepository storeImageRepository;


    public void placeSearch(Map<String,Object> searchData) throws IOException {

        List<Map<String,Object>> searchPlaceData =  (List<Map<String,Object>>) searchData.get("results");

        List<String> placeIdList = new ArrayList<>();

        for (Map<String,Object> arr : searchPlaceData){
            placeIdList.add((String) arr.get("place_id"));
        }

        List<GoogleStoreDto> googleStoreDtoList = detailsPlaceData(placeIdList);

        for (GoogleStoreDto dto : googleStoreDtoList){
            System.out.println(dto);
        }

        photosPlaceData(googleStoreDtoList);


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

    private void photosPlaceData(List<GoogleStoreDto> googleStoreDtoList) throws IOException {

        List<Store> findStoreList = storeRepository.findAll();

        if (googleStoreDtoList != null && !googleStoreDtoList.isEmpty()){
            for (GoogleStoreDto dto : googleStoreDtoList){
                for (Store store : findStoreList){

                    //위도 경로로 매핑
                    if (store.getLatY() != null) {
                        double differenceX = Math.abs((dto.getLngX() - store.getLngX()));
                        double differenceY = Math.abs((dto.getLatY() - store.getLatY()));

                        if (differenceX+differenceY <= 0.0005){
                            System.out.println("==========================");
                            System.out.println(differenceX+differenceY);

                            saveBusinessHoursAndImage(dto, store);
                            break;
                        }
                    }

                    //전화번호로 매핑
                    if (store.getPhone() != null && !store.getPhone().isBlank()
                            && dto.getPhone() != null && !dto.getPhone().isBlank()){

                        if (dto.getPhone().equals(store.getPhone())){

                            saveBusinessHoursAndImage(dto, store);
                            break;
                        }
                    }

                }

            }
        }
    }

    private void saveBusinessHoursAndImage(GoogleStoreDto dto, Store store) throws IOException {

        if (store.getBusinessHours() != null && dto.getBusinessHours() != null){
            BusinessHours businessHours = dto.getBusinessHours();

            businessHoursRepository.save(businessHours);

            store.changeBusinessHours(businessHours);

            System.out.println(businessHours);
        }


//        if (storeImageRepository.findByStore(store).){
//            System.out.println("//////////////");
//            System.out.println(store);
//            List<StoreImage> storeImages = storeImageRepository.findByStore(store).get();
//            for (StoreImage i : storeImages){
//                System.out.println(i);
//            }
//        }
        if (storeImageRepository.findByStore(store).isEmpty()) {
            //이미지 저장
            photoApi(store, dto);
        }
    }

    private String uploadFolder = System.getProperty("user.home");
    private void photoApi(Store store, GoogleStoreDto dto) throws IOException {
        System.out.println("111");
        if (dto.getPhotoReferenceList() != null & !dto.getPhotoReferenceList().isEmpty()) {
            System.out.println("123");
            int i = 0;

            for (String pr : dto.getPhotoReferenceList()) {
                System.out.println(pr);

                byte[] response = rt.getForObject(
                        "https://maps.googleapis.com/maps/api/place/photo" +
                                "?maxwidth=400" +
                                "&photo_reference=" + pr +
                                "&key=" + googleApiKey,
                        byte[].class
                );

                String imageUrl = s3Upload(store, response);

                storeImageRepository.save(new StoreImage(store,imageUrl));

                i++;
                if (i == 5) break;
            }
        }
    }

    private String s3Upload(Store store, byte[] bytes) throws IOException {
        File fileDir = new File(uploadFolder,"data-set");

        //폴더 경로
        String folderPath = "store/" + store.getAddress().getSggNm();

        //파일 이름
        String frontName = store.getStoreId().toString();
        String storeFilename = frontName + "_" + UUID.randomUUID().toString() + "_" + store.getStoreName();

        if (!fileDir.isDirectory()){
            fileDir.mkdirs();
        }

        File fileData = new File(uploadFolder+"/data-set",storeFilename+".png");

        if (!fileData.exists()){
            FileOutputStream fos = new FileOutputStream(fileData);
            fos.write(bytes);
            fos.close();
        }

        //storeKey
        String storeKey = folderPath + "/" + storeFilename;

        //s3 업로드
        String imageUrl = s3Uploader.putS3(fileData, storeKey);

        if (fileData.delete()) {
            System.out.println("파일이 삭제되었습니다.");
        } else {
            System.out.println("파일이 삭제되지 못했습니다.");
        }

        return imageUrl;
    }
}
