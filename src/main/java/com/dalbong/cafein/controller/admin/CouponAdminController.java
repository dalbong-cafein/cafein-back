package com.dalbong.cafein.controller.admin;

import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.admin.coupon.AdminCouponListResDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.service.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class CouponAdminController {

    private final CouponService couponService;

    /**
     * 쿠폰 상태 변경
     */
    @PatchMapping("/coupons/{couponId}")
    public ResponseEntity<?> issue(@PathVariable("couponId") Long couponId){

        couponService.issue(couponId);

        return new ResponseEntity<>(new CMRespDto<>(1, "쿠폰 상태 변경 성공", null), HttpStatus.OK);
    }

    /**
     * 관리자단 쿠폰 리스트 조회
     */
    @GetMapping("/coupons")
    public ResponseEntity<?> getAllCouponList(PageRequestDto requestDto){

        AdminCouponListResDto adminCouponListResDto = couponService.getCouponListOfAdmin(requestDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 쿠폰 리스트 조회 성공", adminCouponListResDto), HttpStatus.OK);
    }

    /**
     * 관리자단 쿠폰 리스트 사용자 지정 조회
     */
    @GetMapping("/coupons/limit")
    public ResponseEntity<?> getCustomLimitCouponList(@RequestParam(required = false, defaultValue = "6") int limit){

        AdminCouponListResDto adminCouponListResDto = couponService.getCustomLimitCouponListOfAdmin(limit);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 쿠폰 리스트 개수 지정 조회 성공", adminCouponListResDto), HttpStatus.OK);
    }
}
