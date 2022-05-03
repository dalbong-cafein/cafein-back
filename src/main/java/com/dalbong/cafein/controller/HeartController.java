package com.dalbong.cafein.controller;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.service.heart.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class HeartController {

    private final HeartService heartService;

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
