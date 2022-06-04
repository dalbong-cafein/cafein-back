package com.dalbong.cafein.service.coupon;

import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.domain.coupon.CouponRepository;
import com.dalbong.cafein.domain.notice.CouponNoticeRepository;
import com.dalbong.cafein.dto.admin.coupon.AdminCouponListResDto;
import com.dalbong.cafein.dto.coupon.CouponRegDto;
import com.dalbong.cafein.dto.admin.coupon.AdminCouponResDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.page.PageResultDTO;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Transactional
@RequiredArgsConstructor
@Service
public class CouponServiceImpl implements CouponService{

    private final CouponRepository couponRepository;
    private final NoticeService noticeService;


    /**
     * 쿠폰 발급 신청
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

        noticeService.registerCouponNotice(coupon, coupon.getMember());
    }

    /**
     * 쿠폰 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminCouponListResDto getCouponListOfAdmin(PageRequestDto pageRequestDto) {

        Pageable pageable;

        if(pageRequestDto.getSort().equals("ASC")){
            pageable = pageRequestDto.getPageable(Sort.by("couponId").ascending());
        }else{
            pageable = pageRequestDto.getPageable(Sort.by("couponId").descending());
        }

        Page<Coupon> results = couponRepository.getCouponList(pageRequestDto.getSearchType(), pageRequestDto.getKeyword(), pageable);

        Function<Coupon, AdminCouponResDto> fn = (c-> new AdminCouponResDto(c));

        return new AdminCouponListResDto(results.getTotalElements(), new PageResultDTO<>(results, fn));
    }

    /**
     * 쿠폰 리스트 사용자 지정 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminCouponListResDto getCustomLimitCouponListOfAdmin(int limit) {

        List<Coupon> results = couponRepository.getCustomLimitCouponList(limit);

        results.stream().map(c -> new )


        return null;
    }
}
