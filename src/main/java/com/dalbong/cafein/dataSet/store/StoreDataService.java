package com.dalbong.cafein.dataSet.store;

import com.dalbong.cafein.dataSet.util.excel.ExcelRead;
import com.dalbong.cafein.dataSet.util.json.JsonUtil;
import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.businessHours.BusinessHoursRepository;
import com.dalbong.cafein.domain.businessHours.Day;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.businessHours.BusinessHoursUpdateDto;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.service.image.ImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class StoreDataService {

    private final ExcelRead excelRead;
    private final StoreRepository storeRepository;
    private final ImageService imageService;
    private final JsonUtil jsonUtil;
    private final ObjectMapper objectMapper;
    private final BusinessHoursRepository businessHoursRepository;

    @Transactional
    public void updatePhoneDataByExcel(MultipartFile excelFile) throws IOException {
       
        List<ExcelStoreDataDto> excelStoreDataDtoList = excelRead.readStorePhoneData(excelFile);

        List<Store> storeList = storeRepository.findByPhoneIsNullOrBlank();

        for (Store store : storeList){

            for(ExcelStoreDataDto excelStoreDataDto : excelStoreDataDtoList){

                if (store.getStoreName().equals(excelStoreDataDto.getStoreName())){

                    if(!excelStoreDataDto.getPhone().equals("-") && !excelStoreDataDto.getPhone().equals("-(폐업)")){

                        //하이픈 추가
                        String hyphenPhone = insertHyphen(excelStoreDataDto.getPhone());

                        store.changePhone(hyphenPhone);

                        System.out.println("storeName: " + store.getStoreName() + " | " +
                                "phone: " + hyphenPhone);
                    }
                }
            }
        }
    }

    @Transactional
    public void saveStoreImageByLocal() throws IOException {
        String path = "C:/cafein_store_image/동대문구";
        File folder = new File(path);

        if(folder.exists()){
            File[] files = folder.listFiles();

            for(File file : files){

                //이미지 이름 - (storeId_?)
                String filename = file.getName();

                Long storeId = Long.parseLong(filename.substring(0, 4));

                Store store = storeRepository.findById(storeId).orElseThrow(() ->
                        new CustomException("존재하지 않는 카페입니다."));

                imageService.saveStoreImage(store, file);
            }
        }
    }

    @Transactional
    public void savePhoneAndBusinessHours() throws IOException, ParseException {

        Object obj = jsonUtil.read("c:/cafein_crawling/store_info_data.json");


        JSONArray jsonArray = (JSONArray) obj;

        if (jsonArray.size() > 0) {

            for(int i = 0; i < jsonArray.size(); i++) {

                JSONObject jsonObj = (JSONObject) jsonArray.get(i);

                JsonStoreDataDto jsonStoreDataDto = objectMapper.readValue(jsonObj.toJSONString(), JsonStoreDataDto.class);

                Store store = storeRepository.findByStoreName(jsonStoreDataDto.getStoreName()).orElseThrow(() ->
                        new CustomException("존재하지 않는 카페입니다."));

                //phone 데이터
                if(jsonStoreDataDto.getPhone() != null && !jsonStoreDataDto.getPhone().isBlank()){
                    store.changePhone(jsonStoreDataDto.getPhone());
                }

                //영업시간 데이터
                BusinessHours findBusinessHours = store.getBusinessHours();
                BusinessHoursUpdateDto businessHoursUpdateDto = jsonStoreDataDto.getBusinessHours();

                //기존 영업시간 데이터가 있는 경우
                if(findBusinessHours != null){
                    findBusinessHours.changeOnMon(businessHoursUpdateDto.getOnMon());
                    findBusinessHours.changeOnTue(businessHoursUpdateDto.getOnTue());
                    findBusinessHours.changeOnWed(businessHoursUpdateDto.getOnWed());
                    findBusinessHours.changeOnThu(businessHoursUpdateDto.getOnThu());
                    findBusinessHours.changeOnFri(businessHoursUpdateDto.getOnFri());
                    findBusinessHours.changeOnSat(businessHoursUpdateDto.getOnSat());
                    findBusinessHours.changeOnSun(businessHoursUpdateDto.getOnSun());
                    findBusinessHours.changeEtcTime(businessHoursUpdateDto.getEtcTime());
                }
                //기존 영업시간 데이터가 없는 경우
                else{
                    BusinessHours businessHours = BusinessHours.builder()
                            .onMon(businessHoursUpdateDto.getOnMon())
                            .onTue(businessHoursUpdateDto.getOnTue())
                            .onWed(businessHoursUpdateDto.getOnWed())
                            .onThu(businessHoursUpdateDto.getOnThu())
                            .onFri(businessHoursUpdateDto.getOnFri())
                            .onSat(businessHoursUpdateDto.getOnSat())
                            .onSun(businessHoursUpdateDto.getOnSun())
                            .etcTime(businessHoursUpdateDto.getEtcTime())
                            .build();

                    businessHoursRepository.save(businessHours);
                    store.changeBusinessHours(businessHours);
                }
            }
        }
    }

    private String insertHyphen(String phone) {

        if(phone.length()==8){
            return phone.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
        }else if(phone.length()==12){
            return phone.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$","$1-$2-$3");
        }
        return phone.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
    }
}
