package com.dalbong.cafein.domain.sticker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CongestionStickerRepository extends JpaRepository<CongestionSticker, Long> {

    @Query("select cs from CongestionSticker cs where cs.congestion.congestionId =:congestionId and cs.member.memberId=:memberId")
    Optional<Sticker> findByCongestionIdAndMemberId(@Param("congestionId") Long congestionId, @Param("memberId") Long memberId);

}
