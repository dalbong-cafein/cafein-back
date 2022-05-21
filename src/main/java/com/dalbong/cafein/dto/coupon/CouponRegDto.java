package com.dalbong.cafein.dto.coupon;

import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CouponRegDto {

    @NotBlank
    private String brandName;

    @NotBlank
    private String itemName;

    public Coupon toEntity(Long principalId){

        return Coupon.builder()
                .member(Member.builder().memberId(principalId).build())
                .brandName(this.brandName)
                .itemName(this.itemName)
                .status(false)
                .processingDateTime(LocalDateTime.now())
                .build();
    }
}
