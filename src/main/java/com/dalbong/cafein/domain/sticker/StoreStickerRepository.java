package com.dalbong.cafein.domain.sticker;

import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StoreStickerRepository extends JpaRepository<StoreSticker, Long> {

    Optional<StoreSticker> findByStore(Store store);
}
