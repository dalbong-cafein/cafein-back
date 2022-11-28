package com.dalbong.cafein.controller.admin;

import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.admin.sticker.AdminStickerResDto;
import com.dalbong.cafein.service.sticker.StickerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class StickerAdminController {

    private final StickerService stickerService;

    /**
     * 관리자단 스티커 회수
     */
    @DeleteMapping("/stickers/{stickerId}")
    public ResponseEntity<?> recoverSticker(@PathVariable("stickerId") Long stickerId){

        stickerService.recover(stickerId);

        return new ResponseEntity<>(new CMRespDto<>(1, "스티커 회수 성공", null), HttpStatus.OK);
    }

    /**
     * 관리자단 회원별 스티커 내역 조회
     */
    @GetMapping("/stickers")
    public ResponseEntity<?> getStickerList(@RequestParam("memberId") Long memberId){

        List<AdminStickerResDto> adminStickerResDtoList = stickerService.getStickerListOfAdmin(memberId);

        return new ResponseEntity<>(new CMRespDto<>(
                1, "관리자단 회원별 스티커 내역 조회 성공", adminStickerResDtoList), HttpStatus.OK);
    }

}
