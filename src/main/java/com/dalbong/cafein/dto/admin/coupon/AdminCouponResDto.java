package com.dalbong.cafein.dto.admin.coupon;

import com.dalbong.cafein.domain.coupon.Coupon;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminCouponResDto {

    private Long couponId;

    private String brandName;

    private String itemName;

    private Long memberId;

    private String phone;

    private boolean status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    private Long memoId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime processingDateTime;

    public AdminCouponResDto(Coupon coupon, Long memoId){
        this.couponId = coupon.getCouponId();
        this.brandName = coupon.getBrandName();
        this.itemName = coupon.getItemName();
        this.memberId = coupon.getMember().getMemberId();
        this.phone = coupon.getMember().getPhone();
        this.status = coupon.getStatus();
        this.regDateTime = coupon.getRegDateTime();
        this.processingDateTime = coupon.getProcessingDateTime();
        this.memoId = memoId;
    }

    public AdminCouponResDto(Coupon coupon){
        this(coupon, null);
    }


}
