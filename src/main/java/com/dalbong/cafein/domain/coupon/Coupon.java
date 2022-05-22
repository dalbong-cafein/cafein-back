package com.dalbong.cafein.domain.coupon;

import com.dalbong.cafein.domain.BaseEntity;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.handler.exception.CustomException;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "member")
@Entity
public class Coupon extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String brandName;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Boolean status;

    private LocalDateTime processingDateTime;

    public void issue(){
        if (this.status) {
            throw new CustomException("이미 발급된 쿠폰입니다.");
        }

        this.status = true;
        this.processingDateTime = LocalDateTime.now();
    }

}
