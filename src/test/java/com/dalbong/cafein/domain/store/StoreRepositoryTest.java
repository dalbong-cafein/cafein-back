package com.dalbong.cafein.domain.store;

import com.dalbong.cafein.domain.store.dto.NearStoreDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class StoreRepositoryTest {

    @Autowired StoreRepository storeRepository;

    @Test
    void 가까운_카페_조회() throws Exception{
        //given
        
        //when

        List<Store> nearStoreDtoList = storeRepository.recommendNearStore(1L, 37.65640383683467, 127.0626695058445);

        //then
        System.out.println(nearStoreDtoList.size());
        for (Store store : nearStoreDtoList){
            System.out.println(store);
        }
    }

}