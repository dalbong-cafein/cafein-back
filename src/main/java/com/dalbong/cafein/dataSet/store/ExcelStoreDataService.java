package com.dalbong.cafein.dataSet.store;

import com.dalbong.cafein.dataSet.util.excel.ExcelRead;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ExcelStoreDataService {

    private final ExcelRead excelRead;
    private final StoreRepository storeRepository;

    @Transactional
    public void updatePhoneData(MultipartFile excelFile, Member member) throws IOException {

        List<ExcelStoreDataDto> excelStoreDataDtoList = excelRead.readStorePhoneData(excelFile);

        List<Store> storeList = storeRepository.findByPhoneIsNullOrBlank();

        for (Store store : storeList){
            for(ExcelStoreDataDto excelStoreDataDto : excelStoreDataDtoList){

                if (store.getStoreName().equals(excelStoreDataDto.getStoreName())){

                    if(!excelStoreDataDto.getPhone().equals("-") && !excelStoreDataDto.getPhone().equals("-(폐업)")){
                        store.changePhone(excelStoreDataDto.getPhone());

                        System.out.println("storeName: " + store.getStoreName() + " | " +
                                "phone: " + store.getPhone());
                    }
                }



            }
        }


    }

}
