package com.dalbong.cafein.service.coupon;

import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.domain.coupon.CouponRepository;
import com.dalbong.cafein.dto.coupon.CouponRegDto;
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
    public Coupon issue(CouponRegDto couponRegDto, Long principalId) {

        Coupon coupon = couponRegDto.toEntity(principalId);

        return couponRepository.save(coupon);
    }
}
