package com.dalbong.cafein.dto.coupon;

import com.dalbong.cafein.domain.coupon.Coupon;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CouponResDto {

    private Long couponId;

    private String brandName;

    private String itemName;

    private Boolean status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    public CouponResDto(Coupon coupon){

        this.couponId = coupon.getCouponId();
        this.brandName = coupon.getBrandName();
        this.itemName = coupon.getItemName();
        this.status = coupon.getStatus();
        this.regDateTime = coupon.getRegDateTime();
    }
}
