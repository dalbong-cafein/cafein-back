package com.dalbong.cafein.domain.sticker;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.dalbong.cafein.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CongestionStickerRepository extends JpaRepository<CongestionSticker, Long> {
    Optional<CongestionSticker> findByCongestion(Congestion congestion);
}
