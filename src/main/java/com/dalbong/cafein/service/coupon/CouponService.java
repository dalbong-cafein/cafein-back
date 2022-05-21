package com.dalbong.cafein.service.coupon;

import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.dto.admin.coupon.AdminCouponListDto;
import com.dalbong.cafein.dto.coupon.CouponRegDto;
import com.dalbong.cafein.dto.admin.coupon.AdminCouponResDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import org.springframework.data.domain.Page;

public interface CouponService {

    Coupon requestCoupon(CouponRegDto couponRegDto, Long principalId);

    void issue(Long couponId);

    AdminCouponListDto getCouponListOfAdmin(PageRequestDto pageRequestDto);
}
