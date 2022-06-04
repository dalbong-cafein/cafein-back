package com.dalbong.cafein.domain.sticker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CongestionStickerRepository extends JpaRepository<CongestionSticker, Long> {

}
