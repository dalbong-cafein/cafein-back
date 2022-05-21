package com.dalbong.cafein.domain.sticker;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StickerRepository extends JpaRepository<Sticker, Long>, StickerRepositoryQuerydsl {


}
