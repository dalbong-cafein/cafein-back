package com.dalbong.cafein.controller.admin;

import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.admin.event.AdminEventListResDto;
import com.dalbong.cafein.dto.event.EventRegDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.service.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class EventAdminController {

    private final EventService eventService;

    /**
     * 관리자단 이벤트 배너 저장
     */
    @PostMapping("/events")
    public ResponseEntity<?> registerEventImage(@Validated EventRegDto eventRegDto, BindingResult bindingResult) throws IOException {

        eventService.register(eventRegDto.getImageFile(), eventRegDto.getBoardId());

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 이벤트 배너 저장 성공", null), HttpStatus.CREATED);
    }

    /**
     * 관리자단 이벤트 이미지 삭제
     */
    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<?> removeEventImage(@PathVariable("eventId") Long eventId){

        eventService.remove(eventId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 이벤트 삭제 성공", null), HttpStatus.OK);
    }

    /**
     * 관리자단 이벤트 배너 리스트 조회
     */
    @GetMapping("/events")
    public ResponseEntity<?> getEventImageList(PageRequestDto requestDto){

        AdminEventListResDto<?> adminEventListResDto = eventService.getEventListOfAdmin(requestDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 이벤트 배너 리스트 조회", adminEventListResDto), HttpStatus.OK);
    }
}
