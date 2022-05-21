package com.dalbong.cafein.service.coupon;

import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.domain.coupon.CouponRepository;
import com.dalbong.cafein.dto.coupon.CouponRegDto;
import com.dalbong.cafein.handler.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CouponServiceImpl implements CouponService{

    private final CouponRepository couponRepository;


    /**
     * 쿠폰 발급
     */
    @Transactional
    @Override
    public Coupon requestCoupon(CouponRegDto couponRegDto, Long principalId) {

        Coupon coupon = couponRegDto.toEntity(principalId);

        return couponRepository.save(coupon);
    }

    /**
     * 쿠폰 발급
     */
    @Transactional
    @Override
    public void issue(Long couponId) {

        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() ->
                new CustomException("존재하는 않는 쿠폰입니다."));

        coupon.issue();
    }
}
