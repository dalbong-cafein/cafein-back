package com.dalbong.cafein.service.coupon;

import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.domain.coupon.CouponRepository;
import com.dalbong.cafein.domain.notice.CouponNoticeRepository;
import com.dalbong.cafein.domain.sticker.StickerRepository;
import com.dalbong.cafein.dto.admin.coupon.AdminCouponListResDto;
import com.dalbong.cafein.dto.coupon.CouponRegDto;
import com.dalbong.cafein.dto.admin.coupon.AdminCouponResDto;
import com.dalbong.cafein.dto.coupon.CouponResDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.page.PageResultDTO;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.service.notice.NoticeService;
import com.dalbong.cafein.service.sticker.StickerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class CouponServiceImpl implements CouponService{

    private final CouponRepository couponRepository;
    private final NoticeService noticeService;
    private final StickerRepository stickerRepository;
    private final StickerService stickerService;


    /**
     * 쿠폰 발급 신청
     */
    @Transactional
    @Override
    public Coupon requestCoupon(CouponRegDto couponRegDto, Long principalId) {

        int stickerCnt = stickerRepository.getCountStickerOfMember(principalId);

        if(stickerCnt < 20){
            throw new CustomException("스티커 보유량이 20개 미만 입니다.");
        }

        //스티커 20개 삭제
        stickerService.removeStickerList(20, principalId);

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
     * 회원별 쿠폰 리스트 조회
     */
    @Transactional
    @Override
    public List<CouponResDto> getCouponList(Long principalId) {

        List<Coupon> couponList = couponRepository.getCouponByMemberId(principalId);

        return couponList.stream().map(CouponResDto::new).collect(Collectors.toList());
    }

    /**
     * 쿠폰 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminCouponListResDto<?> getCouponListOfAdmin(PageRequestDto pageRequestDto) {

        Pageable pageable;

        if(pageRequestDto.getSort().equals("ASC")){
            pageable = pageRequestDto.getPageable(Sort.by("couponId").ascending());
        }else{
            pageable = pageRequestDto.getPageable(Sort.by("couponId").descending());
        }

        Page<Object[]> results = couponRepository.getCouponList(pageRequestDto.getSearchType(), pageRequestDto.getKeyword(), pageable);

        Function<Object[], AdminCouponResDto> fn = (arr-> {

            Coupon coupon = (Coupon) arr[0];

            return new AdminCouponResDto(coupon, (Long) arr[1]);
        });

        return new AdminCouponListResDto<>(results.getTotalElements(), new PageResultDTO<>(results, fn));
    }

    /**
     * 쿠폰 리스트 사용자 개수 지정 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminCouponListResDto<?> getCustomLimitCouponListOfAdmin(int limit) {

        List<Coupon> results = couponRepository.getCustomLimitCouponList(limit);

        List<AdminCouponResDto> adminCouponResDtoList = results.stream().map(c -> new AdminCouponResDto(c)).collect(Collectors.toList());

        return new AdminCouponListResDto<>(results.size(), adminCouponResDtoList);
    }
}
