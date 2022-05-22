package com.dalbong.cafein.dto.admin.coupon;

import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.dto.page.PageResultDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminCouponListDto {

    private long couponCnt;

    private PageResultDTO<AdminCouponResDto, Coupon> couponResDtoList;

}
