package com.dalbong.cafein.dataSet.store;

import com.dalbong.cafein.dataSet.util.excel.ExcelRead;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class StoreDataService {

    private final ExcelRead excelRead;
    private final StoreRepository storeRepository;
    private final ImageService imageService;

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

    private String insertHyphen(String phone) {

        if(phone.length()==8){
            return phone.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
        }else if(phone.length()==12){
            return phone.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$","$1-$2-$3");
        }
        return phone.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
    }
}
