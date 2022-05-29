package com.dalbong.cafein.domain.sticker;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StickerRepository extends JpaRepository<Sticker, Long>, StickerRepositoryQuerydsl {
}
