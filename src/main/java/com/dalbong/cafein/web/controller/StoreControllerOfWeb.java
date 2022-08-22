package com.dalbong.cafein.web.controller;

import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.web.domain.contents.ContentsType;
import com.dalbong.cafein.web.dto.NearStoreResDtoOfWeb;
import com.dalbong.cafein.web.dto.StoreResDtoOfWeb;
import com.dalbong.cafein.web.service.ContentsStoreService;
import com.dalbong.cafein.web.service.StoreServiceOfWeb;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class StoreControllerOfWeb {

    private final StoreServiceOfWeb storeServiceOfWeb;
    private final ContentsStoreService contentsStoreService;

     /**
     * 웹 - 카페 리스트 조회
     */
    @GetMapping("/web/stores")
    public ResponseEntity<?> getStoreList(@RequestParam(value = "keyword", required = false) String keyword){

        List<StoreResDtoOfWeb> storeResDtoOfWebList = storeServiceOfWeb.getStoreListOfWeb(keyword);

        return new ResponseEntity<>(new CMRespDto<>(1, "카페 리스트 조회 성공", storeResDtoOfWebList), HttpStatus.OK);
    }

    /**
     * 웹 - 근처 카공 카페 리스트 조회 - 조회중인 카페 기준
     */
    @GetMapping("/web/stores/{storeId}/near-stores")
    public ResponseEntity<?> getNearStoreList(@PathVariable("storeId") Long storeId){

        List<NearStoreResDtoOfWeb> newStoreResDtoOfWebList = storeServiceOfWeb.getNearStoreList(storeId);

        return new ResponseEntity<>(new CMRespDto<>(1, "근처 카공 카페 리스트 조회 성공", newStoreResDtoOfWebList), HttpStatus.OK);
    }

    /**
     * 웹 - 지역별 컨텐츠 추천 카페 리스트 조회
     */
    @GetMapping("/web/stores/contents")
    public ResponseEntity<?> getContentsStoreList(@RequestParam("sggNm") String sggNm, @RequestParam("type") ContentsType contentsType){

        List<StoreResDtoOfWeb> storeResDtoOfWebList = contentsStoreService.getContentsStoreList(sggNm, contentsType);

        return new ResponseEntity<>(new CMRespDto<>(1, "지역별 컨텐츠 카페 추천 리스트 조회 성공", storeResDtoOfWebList), HttpStatus.OK);
    }
}
