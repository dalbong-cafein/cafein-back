package com.dalbong.cafein.dataSet.store;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional
@RequiredArgsConstructor
@Service
public class ExcelStoreDataService {

    private final StoreRepository storeRepository;

    @Transactional
    public void insertPhoneData(MultipartFile excelFile, Member member){




    }

}
