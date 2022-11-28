package com.dalbong.cafein.controller.admin;

import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.admin.congestion.AdminCongestionListResDto;
import com.dalbong.cafein.dto.admin.congestion.AdminCongestionResDto;
import com.dalbong.cafein.service.congestion.CongestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class CongestionAdminController {

    private final CongestionService congestionService;

    /**
     * 관리자단 카페별 혼잡도 리스트 조회
     */
    @GetMapping("/stores/{storeId}/congestions")
    public ResponseEntity<?> getCongestionList(@PathVariable("storeId") Long storeId,
                                               @RequestParam(required = false, defaultValue = "0") Integer minusDays){

        AdminCongestionListResDto<List<AdminCongestionResDto>> adminCongestionListResDto = congestionService.getCongestionListOfAdmin(storeId, minusDays);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 카페별 혼잡도 리스트 조회 성공", adminCongestionListResDto), HttpStatus.OK);
    }
}
