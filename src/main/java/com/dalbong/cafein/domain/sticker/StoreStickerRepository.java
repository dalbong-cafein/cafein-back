package com.dalbong.cafein.domain.sticker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoreStickerRepository extends JpaRepository<StoreSticker, Long> {

    @Query("select ss from StoreSticker ss where ss.store.storeId =:storeId and ss.member.memberId=:memberId")
    Optional<Sticker> findByStoreIdAndMemberId(@Param("storeId") Long storeId, @Param("memberId") Long memberId);
}
