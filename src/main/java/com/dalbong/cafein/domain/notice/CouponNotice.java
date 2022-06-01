package com.dalbong.cafein.domain.notice;

import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@ToString(exclude = {"coupon"})
@DiscriminatorValue("coupon")
@Entity
public class CouponNotice extends Notice{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    public CouponNotice(Coupon coupon, Member toMember){
        super(toMember,"신청하신 "+ coupon.getBrandName() +" 기프티콘이 지급 완료되었습니다.");
        this.coupon = coupon;
    }

    public CouponNotice(Coupon coupon, Member toMember, String content){
        super(toMember, content);
        this.coupon = coupon;
    }
}
