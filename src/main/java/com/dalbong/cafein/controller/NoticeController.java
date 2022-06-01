package com.dalbong.cafein.controller;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.notice.NoticeResDto;
import com.dalbong.cafein.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/notices")
    public ResponseEntity<?> getNoticeList(@AuthenticationPrincipal PrincipalDetails principalDetails){

        List<NoticeResDto> noticeResDtoList = noticeService.getNoticeList(principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "알림 리스트 조회 성공", noticeResDtoList), HttpStatus.OK);
    }

    @PatchMapping("/notices/{noticeId}/read")
    public ResponseEntity<?> modifyIsRead(@PathVariable("noticeId") Long noticeId){

        noticeService.read(noticeId);

        return new ResponseEntity<>(new CMRespDto<>(1, "알림 읽음 처리 성공", null), HttpStatus.OK);
    }

    @DeleteMapping("/notices/{noticeId}")
    public ResponseEntity<?> remove(@PathVariable("noticeId") Long noticeId){

        noticeService.remove(noticeId);

        return new ResponseEntity<>(new CMRespDto<>(1, "알림 삭제 성공", null), HttpStatus.OK);
    }


}
