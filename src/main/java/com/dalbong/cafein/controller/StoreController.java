package com.dalbong.cafein.controller;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.store.DetailStoreResDto;
import com.dalbong.cafein.dto.store.RecommendSearchStoreResDto;
import com.dalbong.cafein.dto.store.StoreRegDto;
import com.dalbong.cafein.dto.store.StoreResDto;
import com.dalbong.cafein.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class StoreController {

    private final StoreService storeService;

    /**
     * 카페 리스트 조회
     */
    @GetMapping("/stores")
    public ResponseEntity<?> getStoreList(@RequestParam(value = "keyword", required = false) String keyword){

        List<StoreResDto> storeResDtoList = storeService.getStoreList(keyword);

        return new ResponseEntity<>(new CMRespDto<>(1, "카페 리스트 조회 성공", storeResDtoList), HttpStatus.OK);
    }

    /**
     * 추천 검색 카페 리스트 조회
     */
    @GetMapping("/stores/recommend-search")
    public ResponseEntity<?> getRecommendSearchStoreList(@RequestParam(value = "keyword") String keyword){

        List<RecommendSearchStoreResDto> recommendSearchStoreResDtoList =
                storeService.getRecommendSearchStoreList(keyword);

        return new ResponseEntity<>(new CMRespDto<>(
                1, "추천 카페 리스트 조회 성공", recommendSearchStoreResDtoList), HttpStatus.OK);
    }

    /**
     * 카페 상세 조회
     */
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<?> getDetailStore(@PathVariable("storeId") Long storeId,
                                            @AuthenticationPrincipal PrincipalDetails principalDetails){

        DetailStoreResDto detailStoreResDto = storeService.getDetailStore(storeId, principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "카페 상세 조회 성공", detailStoreResDto), HttpStatus.OK);
    }

    /**
     * 가게 등록
     */
    @PostMapping("/stores")
    public ResponseEntity<?> register(@Validated StoreRegDto storeRegDto,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        storeService.register(storeRegDto,principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1,"가게 등록 성공",null), HttpStatus.CREATED);
    }




}
