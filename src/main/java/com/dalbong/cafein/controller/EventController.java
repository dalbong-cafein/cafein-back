package com.dalbong.cafein.controller;

import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.event.EventResDto;
import com.dalbong.cafein.service.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EventController {

    private final EventService eventService;

    /**
     * 가장 최신의 이벤트 배너 조회
     */
    @GetMapping("/events/latest")
    public ResponseEntity<?> getLatestEvent(){

        EventResDto eventResDto = eventService.latestEvent();

        return new ResponseEntity<>(new CMRespDto<>(1, "가장 최신 이벤트 배너 조회 성공", eventResDto), HttpStatus.OK);
    }





}
