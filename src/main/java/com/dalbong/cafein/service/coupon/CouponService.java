package com.dalbong.cafein.service.coupon;

import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.dto.coupon.CouponRegDto;

public interface CouponService {

    Coupon issue(CouponRegDto couponRegDto, Long principalId);
}
