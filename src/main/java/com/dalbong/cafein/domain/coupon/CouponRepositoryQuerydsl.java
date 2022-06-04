package com.dalbong.cafein.domain.coupon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CouponRepositoryQuerydsl {

    /**
     * 관리자단 쿠폰 리스트 조회
     */
    Page<Coupon> getCouponList(String[] searchType, String keyword, Pageable pageable);

    /**
     * 관리자단 쿠폰 리스트 조회
     */
    List<Coupon> getCustomLimitCouponList(int limit);


}
