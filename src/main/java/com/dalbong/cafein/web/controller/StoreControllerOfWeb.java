package com.dalbong.cafein.web.controller;

import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.web.dto.StoreResDtoOfWeb;
import com.dalbong.cafein.web.service.StoreServiceOfWeb;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StoreControllerOfWeb {

    private final StoreServiceOfWeb storeServiceOfWeb;

     /**
     * 웹 - 카페 리스트 조회
     */
    @GetMapping("/web/stores")
    public ResponseEntity<?> getStoreList(@RequestParam(value = "keyword", required = false) String keyword){

        List<StoreResDtoOfWeb> storeResDtoOfWebList = storeServiceOfWeb.getStoreListOfWeb(keyword);

        return new ResponseEntity<>(new CMRespDto<>(1, "카페 리스트 조회 성공", storeResDtoOfWebList), HttpStatus.OK);
    }
}
