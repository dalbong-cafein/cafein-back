package com.dalbong.cafein.web.controller;

import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.web.dto.RecommendRegDto;
import com.dalbong.cafein.web.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RestController
public class RecommendController {

    private final RecommendService recommendService;

    /**
     * 카페 추천 데이터 등록
     */
    @PostMapping("/web/recommendations")
    public ResponseEntity<?> recommend(@RequestBody RecommendRegDto recommendRegDto,
                                        HttpSession httpSession){

        String sessionId = httpSession.getId();

        recommendService.recommend(recommendRegDto, sessionId);

        return new ResponseEntity<>(new CMRespDto<>(1, "추천 데이터 등록 성공", null), HttpStatus.CREATED);
    }

    /**
     * 웹 - 카페 본인의 카페 추천 데이터 조회
     */
    @GetMapping("/web/stores/{storeId}/recommendations")
    public ResponseEntity<?> getRecommendation(@PathVariable("storeId") Long storeId, HttpSession httpSession){


        Recommendation recommendation = recommendService.getRecommendation(storeId, httpSession.getId());

        return new ResponseEntity<>(new CMRespDto<>(1, "추천 데이터 조회 성공", recommendation), HttpStatus.OK);
    }
}
