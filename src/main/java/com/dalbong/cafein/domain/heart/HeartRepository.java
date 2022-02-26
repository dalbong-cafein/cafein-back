package com.dalbong.cafein.domain.heart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    @Modifying
    @Query("delete from Heart h where h.member.memberId = :memberId and h.store.storeId = :storeId")
    void deleteHeart(@Param("memberId") Long memberId, @Param("storeId") Long storeId);
}
