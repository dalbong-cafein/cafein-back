package com.dalbong.cafein.controller.admin;

import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.admin.store.AdminMyStoreResDto;
import com.dalbong.cafein.service.heart.HeartService;
import com.dalbong.cafein.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class HeartAdminController {

    private final StoreService storeService;

    /**
     * 관리자단 회원별 내 카페 리스트 조회
     */
    @GetMapping("/members/{memberId}/hearts")
    public ResponseEntity<?> getMyStoreListByMemberId(@PathVariable("memberId") Long memberId){

        List<AdminMyStoreResDto> adminMyStoreResDtoList = storeService.getMyStoreByMemberIdOfAdmin(memberId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 회원별 내 카페 리스트 조회 성공", adminMyStoreResDtoList), HttpStatus.OK);
    }
}
