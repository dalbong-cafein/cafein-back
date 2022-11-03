package com.dalbong.cafein.domain.coupon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CouponRepositoryQuerydsl {

    /**
     * 관리자단 쿠폰 리스트 조회
     */
    Page<Object[]> getCouponList(String[] searchType, String keyword, Pageable pageable);

    /**
     * 관리자단 쿠폰 리스트 사용자 개수 지정 조회
     */
    List<Coupon> getCustomLimitCouponList(int limit);


}
