package com.dalbong.cafein.domain.coupon;

import com.dalbong.cafein.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponRepositoryQuerydsl{

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Coupon c where c.member =:member")
    void deleteByMember(@Param("member") Member member);
}
