package com.dalbong.cafein.domain.memo;

import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@ToString(exclude = {"coupon"})
@DiscriminatorValue("coupon")
@Entity
public class CouponMemo extends Memo{

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    public CouponMemo(Coupon coupon, Member writer, String content){
        super(writer, content);
        this.coupon = coupon;
    }

}
