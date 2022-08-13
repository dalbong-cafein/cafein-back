package com.dalbong.cafein.controller;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.store.*;
import com.dalbong.cafein.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<?> getStoreList(@RequestParam(value = "keyword", required = false) String keyword,
                                          @AuthenticationPrincipal PrincipalDetails principalDetails){

        List<StoreResDto> storeResDtoList;

        //비로그인 상태
        if(principalDetails == null){
            storeResDtoList = storeService.getStoreList(keyword, null);
        }
        //로그인 상태
        else{
            storeResDtoList = storeService.getStoreList(keyword, principalDetails.getMember().getMemberId());
        }


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
     * 근처 카공 카페 리스트 조회 - 조회중인 카페 기준
     */
    @GetMapping("/stores/{storeId}/recommend")
    public ResponseEntity<?> getRecommendStoreList(@PathVariable("storeId") Long storeId,
                                                   @AuthenticationPrincipal PrincipalDetails principalDetails){

        List<NearStoreResDto> nearStoreResList = storeService.getNearStoreList(storeId, principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "근처 카공 카페 리스트 조회 성공", nearStoreResList), HttpStatus.OK);
    }

    /**
     * 앱단 본인이 등록한 가게 리스트 조회
     */
    @GetMapping("/stores/my-registered")
    public ResponseEntity<?> getMyRegisteredStoreList(@AuthenticationPrincipal PrincipalDetails principalDetails){

        StoreListResDto<List<MyRegisterStoreResDto>> storeListResDto =
                storeService.getMyRegisterStoreList(principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(
                1, "내가 등록한 가게 리스트 조회 성공", storeListResDto), HttpStatus.OK);
    }

    /**
     * 카페 상세 조회
     */
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<?> getDetailStore(@PathVariable("storeId") Long storeId,
                                            @AuthenticationPrincipal PrincipalDetails principalDetails){

        DetailStoreResDto detailStoreResDto;

        //비로그인 상태
        if (principalDetails == null){
            detailStoreResDto = storeService.getDetailStore(storeId, null);
        }
        //로그인 상태
        else{
            detailStoreResDto = storeService.getDetailStore(storeId, principalDetails.getMember().getMemberId());
        }

        return new ResponseEntity<>(new CMRespDto<>(1, "카페 상세 조회 성공", detailStoreResDto), HttpStatus.OK);
    }

    /**
     * 카페 등록
     */
    @PostMapping("/stores")
    public ResponseEntity<?> register(@Validated StoreRegDto storeRegDto, BindingResult bindingResult,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        Store store = storeService.register(storeRegDto, principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1,"카페 등록 성공",store.getStoreId()), HttpStatus.CREATED);
    }

    /**
     * 카페 수정
     */
    @PutMapping("/stores/{storeId}")
    public ResponseEntity<?> modify(@Validated StoreUpdateDto storeUpdateDto, BindingResult bindingResult,
                                    @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        storeService.modify(storeUpdateDto, principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "카페 수정 성공", null), HttpStatus.OK);

    }



}
