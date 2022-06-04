package com.dalbong.cafein.service.coupon;

import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.dto.admin.coupon.AdminCouponListResDto;
import com.dalbong.cafein.dto.coupon.CouponRegDto;
import com.dalbong.cafein.dto.page.PageRequestDto;

public interface CouponService {

    Coupon requestCoupon(CouponRegDto couponRegDto, Long principalId);

    void issue(Long couponId);

    AdminCouponListResDto getCouponListOfAdmin(PageRequestDto pageRequestDto);

    AdminCouponListResDto getCustomLimitCouponListOfAdmin(int limit);
}
