package com.dalbong.cafein.controller;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.notice.NoticeResDto;
import com.dalbong.cafein.dto.notice.detailReportNotice.DetailReportNoticeResDto;
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

    /**
     * 알림 리스트 조회
     */
    @GetMapping("/notices")
    public ResponseEntity<?> getNoticeList(@AuthenticationPrincipal PrincipalDetails principalDetails){

        List<NoticeResDto> noticeResDtoList = noticeService.getNoticeList(principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "알림 리스트 조회 성공", noticeResDtoList), HttpStatus.OK);
    }

    /**
     * 신고 알림 상세 조회
     */
    @GetMapping("/notices/reportType/{noticeId}")
    public ResponseEntity<?> getReportNotice(@PathVariable("noticeId") Long noticeId){

        DetailReportNoticeResDto detailReportNoticeResDto = noticeService.getDetailReportNotice(noticeId);

        return new ResponseEntity<>(new CMRespDto<>(1, "신고 알림 상세 조회 성공", detailReportNoticeResDto), HttpStatus.OK);
    }

    /**
     * 알림 읽음 처리
     */
    @PatchMapping("/notices/{noticeId}/read")
    public ResponseEntity<?> modifyIsRead(@PathVariable("noticeId") Long noticeId){

        noticeService.read(noticeId);

        return new ResponseEntity<>(new CMRespDto<>(1, "알림 읽음 처리 성공", null), HttpStatus.OK);
    }

    /**
     * 알림 삭제
     */
    @DeleteMapping("/notices/{noticeId}")
    public ResponseEntity<?> remove(@PathVariable("noticeId") Long noticeId){

        noticeService.remove(noticeId);

        return new ResponseEntity<>(new CMRespDto<>(1, "알림 삭제 성공", null), HttpStatus.OK);
    }

    /**
     * 회원별 전체 알림 삭제
     */
    @DeleteMapping("/notices")
    public ResponseEntity<?> removeAll(@AuthenticationPrincipal PrincipalDetails principalDetails){

        noticeService.removeAll(principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "회원별 전체 알림 삭제 성공", null), HttpStatus.OK);
    }


}
