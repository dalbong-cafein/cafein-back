package com.dalbong.cafein.controller;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.store.MyStoreResDto;
import com.dalbong.cafein.dto.store.StoreListResDto;
import com.dalbong.cafein.service.heart.HeartService;
import com.dalbong.cafein.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class HeartController {

    private final HeartService heartService;
    private final StoreService storeService;

    /**
     * 내 카페 리스트 조회
     */
    @GetMapping("/hearts")
    public ResponseEntity<?> getMyStoreList(@AuthenticationPrincipal PrincipalDetails principalDetails){

        StoreListResDto<List<MyStoreResDto>> storeListResDto = storeService.getMyStoreList(principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "내 카페 리스트 조회 성공", storeListResDto), HttpStatus.OK);
    }

    /**
     * 앱단 내 가게 리스트 개수지정 조회
     */
    @GetMapping("/hearts/limit")
    public ResponseEntity<?> getCustomLimitMyStoreList(@RequestParam(value = "limit",defaultValue = "3", required = false) int limit,
                                                                 @AuthenticationPrincipal PrincipalDetails principalDetails){

        StoreListResDto<List<MyStoreResDto>> storeListResDto = storeService.getCustomLimitMyStoreList(limit, principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(
                1, "내 가게 리스트 개수지정 조회 성공", storeListResDto), HttpStatus.OK);
    }

    /**
     * 내 카페 등록
     */
    @PostMapping("/stores/{storeId}/hearts")
    public ResponseEntity<?> heart(@PathVariable("storeId") Long storeId,
                                   @AuthenticationPrincipal PrincipalDetails principalDetails){

        heartService.heart(principalDetails.getMember().getMemberId(), storeId);

        return new ResponseEntity<>(new CMRespDto<>(1, "내 카페 등록 성공", null), HttpStatus.CREATED);
    }

    /**
     * 내 카페 취소
     */
    @DeleteMapping("/stores/{storeId}/hearts")
    public ResponseEntity<?> cancelHeart(@PathVariable("storeId") Long storeId,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails){

        heartService.cancelHeart(principalDetails.getMember().getMemberId(), storeId);

        return new ResponseEntity<>(new CMRespDto<>(1, "내 카페 취소 성공", null), HttpStatus.OK);
    }


}
