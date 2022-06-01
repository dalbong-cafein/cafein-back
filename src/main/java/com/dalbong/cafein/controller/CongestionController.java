package com.dalbong.cafein.controller;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.domain.congestion.Congestion;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.congestion.CongestionListResDto;
import com.dalbong.cafein.dto.congestion.CongestionRegDto;
import com.dalbong.cafein.dto.congestion.CongestionResDto;
import com.dalbong.cafein.service.congestion.CongestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CongestionController {

    private final CongestionService congestionService;

    /**
     * 혼잡도 등록
     */
    @PostMapping("/congestion")
    public ResponseEntity<?> register(@Validated @RequestBody CongestionRegDto congestionRegDto, BindingResult bindingResult,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails){

        Congestion congestion = congestionService.register(congestionRegDto, principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "혼잡도 등록 성공",congestion.getCongestionId()), HttpStatus.CREATED);
    }

    /**
     * 카페별 혼잡도 리스트 조회
     */
    @GetMapping("stores/{storeId}/congestion")
    public ResponseEntity<?> getCongestionList(@PathVariable("storeId") Long storeId,
                                               @RequestParam(required = false, defaultValue = "0") Integer minusDays){

        CongestionListResDto<List<CongestionResDto>> congestionListResDto = congestionService.getCongestionList(storeId, minusDays);

        return new ResponseEntity<>(new CMRespDto<>(1, "혼잡도 리스트 조회 성공", congestionListResDto), HttpStatus.OK);
    }

}
