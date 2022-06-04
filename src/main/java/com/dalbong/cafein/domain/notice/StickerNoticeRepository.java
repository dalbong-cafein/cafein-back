package com.dalbong.cafein.domain.notice;

import com.dalbong.cafein.domain.sticker.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StickerNoticeRepository extends JpaRepository<StickerNotice,Long> {

    void deleteBySticker(Sticker sticker);

}
