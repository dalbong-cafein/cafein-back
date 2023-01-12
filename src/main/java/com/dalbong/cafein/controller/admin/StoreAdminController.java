package com.dalbong.cafein.controller.admin;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.admin.store.AdminDetailStoreResDto;
import com.dalbong.cafein.dto.admin.store.AdminRepresentImageSetUpDto;
import com.dalbong.cafein.dto.admin.store.AdminStoreListDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.store.StoreRegDto;
import com.dalbong.cafein.dto.store.StoreUpdateDto;
import com.dalbong.cafein.service.image.ImageService;
import com.dalbong.cafein.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class StoreAdminController {

    private final StoreService storeService;
    private final ImageService imageService;

    /**
     * 관리자단 카페 등록
     */
    @PostMapping("/stores")
    public ResponseEntity<?> registerStore(@Validated StoreRegDto storeRegDto, BindingResult bindingResult,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        //storeService.registerOfAdmin(storeRegDto,principalDetails.getMember().getMemberId());
        storeService.registerOfAdmin(storeRegDto,1L);

        return new ResponseEntity<>(new CMRespDto<>(1,"관리자단 카페 등록 성공",null), HttpStatus.CREATED);
    }

    /**
     * 관리자단 카페 수정
     */
    @PutMapping("/stores/{storeId}")
    public ResponseEntity<?> modifyStore(@Validated StoreUpdateDto storeUpdateDto, BindingResult bindingResult,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        //storeService.modifyOfAdmin(storeUpdateDto, principalDetails.getMember().getMemberId());
        storeService.modifyOfAdmin(storeUpdateDto, 1L);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 카페 수정 성공", null), HttpStatus.OK);

    }

    /**
     * 관리자단 카페 대표 이미지 설정
     */
    @PatchMapping("/stores/{storeId}/representation-image")
    public ResponseEntity<?> setUpRepresentativeImage(@Validated @RequestBody AdminRepresentImageSetUpDto adminRepresentImageSetUpDto, BindingResult bindingResult){

        imageService.setUpRepresentativeImageOfStore(adminRepresentImageSetUpDto.getStoreId(),
                adminRepresentImageSetUpDto.getRepresentImageId());

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 카페 대표 이미지 설정 성공",null), HttpStatus.OK);
    }


    /**
     * 관리자단 카페 삭제
     */
    @DeleteMapping("/stores/{storeId}")
    public ResponseEntity<?> removeStore(@PathVariable("storeId") Long storeId){

        storeService.remove(storeId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 카페 삭제 성공", null), HttpStatus.OK);
    }

    /**
     * 관리자단 카페 리스트 조회
     */
    @GetMapping("/stores")
    public ResponseEntity<?> getAllStoreList(PageRequestDto requestDto){

        AdminStoreListDto adminStoreListDto = storeService.getStoreListOfAdmin(requestDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 카페 리스트 조회 성공", adminStoreListDto), HttpStatus.OK);
    }

    /**
     * 관리자단 카페 상세 조회
     */
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<?> getDetailStore(@PathVariable("storeId") Long storeId){

        AdminDetailStoreResDto adminDetailStoreResDto = storeService.getDetailStoreOfAdmin(storeId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 카페 상세 조회 성공", adminDetailStoreResDto), HttpStatus.OK);
    }
}
