package com.dalbong.cafein.web.controller;

import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.web.dto.RecommendRegDto;
import com.dalbong.cafein.web.dto.RecommendResDto;
import com.dalbong.cafein.web.service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

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
     * 웹 - 카페별 추천 관련 데이터 조회
     */
    @GetMapping("/web/stores/{storeId}/recommendations")
    public ResponseEntity<?> getRecommendation(@PathVariable("storeId") Long storeId, HttpSession httpSession){

        //카페 추천율
        Double recommendPercentOfStore = recommendService.getRecommendPercent(storeId);

        //본인이 등록한 추천 데이터
        Optional<Recommendation> result = recommendService.getRecommendation(storeId, httpSession.getId());

        RecommendResDto recommendResDto;

        //추천 데이터를 등록한 경우
        if(result.isPresent()){
            recommendResDto = new RecommendResDto(recommendPercentOfStore, result.get());
        }
        //추천 데이터를 등록하지 않은 경우
        else {
            recommendResDto = new RecommendResDto(recommendPercentOfStore, null);
        }

        return new ResponseEntity<>(new CMRespDto<>(1, "카페별 추천 관련 데이터 조회 성공", recommendResDto), HttpStatus.OK);
    }
}
