package com.dalbong.cafein.controller;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.coupon.CouponRegDto;
import com.dalbong.cafein.service.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CouponController {

    private final CouponService couponService;

    /**
     * 쿠폰 발급
     */
    @PostMapping("/coupons")
    public ResponseEntity<?> issue(@RequestBody CouponRegDto couponRegDto,
                                   @AuthenticationPrincipal PrincipalDetails principalDetails){

        couponService.issue(couponRegDto, principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "쿠폰 발급 성공", null), HttpStatus.CREATED);
    }

}
