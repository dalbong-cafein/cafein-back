package com.dalbong.cafein.controller;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.store.StoreRegDto;
import com.dalbong.cafein.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class StoreController {

    private final StoreService storeService;

    /**
     * 관리자단 가게 등록
     */
    @PostMapping("/admin/stores")
    public ResponseEntity<?> register(@Validated StoreRegDto storeRegDto) throws IOException {

        storeService.registerByAdmin(storeRegDto);

        return new ResponseEntity<>(new CMRespDto<>(1,"가게 등록 성공",null), HttpStatus.CREATED);
    }

    /**
     * 가게 승인 여부 수정
     */
    @PatchMapping("/admin/stores/{storeId}/isApproval")
    public ResponseEntity<?> modifyIsApproval(@PathVariable("storeId") Long storeId){

        storeService.modifyIsApproval(storeId);

        return new ResponseEntity<>(new CMRespDto<>(1, "가게 승인여부 수정 성공", null),HttpStatus.OK);
    }

}
