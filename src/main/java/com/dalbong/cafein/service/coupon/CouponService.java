package com.dalbong.cafein.service.coupon;

import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.dto.coupon.CouponRegDto;

public interface CouponService {

    Coupon requestCoupon(CouponRegDto couponRegDto, Long principalId);

    void issue(Long couponId);
}
