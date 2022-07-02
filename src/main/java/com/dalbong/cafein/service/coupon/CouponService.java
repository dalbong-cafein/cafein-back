package com.dalbong.cafein.service.coupon;

import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.dto.admin.coupon.AdminCouponListResDto;
import com.dalbong.cafein.dto.coupon.CouponRegDto;
import com.dalbong.cafein.dto.coupon.CouponResDto;
import com.dalbong.cafein.dto.page.PageRequestDto;

import java.util.List;

public interface CouponService {

    Coupon requestCoupon(CouponRegDto couponRegDto, Long principalId);

    void issue(Long couponId);

    List<CouponResDto> getCouponList(Long principalId);

    AdminCouponListResDto getCouponListOfAdmin(PageRequestDto pageRequestDto);

    AdminCouponListResDto getCustomLimitCouponListOfAdmin(int limit);
}
