package com.dalbong.cafein.controller;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.store.StoreRegDto;
import com.dalbong.cafein.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class StoreController {

    private final StoreService storeService;

    /**
     * 가게 등록
     */
    @PostMapping("/stores")
    public ResponseEntity<?> register(@Validated StoreRegDto storeRegDto,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails){

        storeService.register(storeRegDto,2L);

        return new ResponseEntity<>(new CMRespDto<>(1,"가게 등록 성공",null), HttpStatus.CREATED);
    }

}
