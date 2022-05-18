package com.dalbong.cafein.service.sticker;

import com.dalbong.cafein.domain.sticker.Sticker;
import com.dalbong.cafein.domain.sticker.StickerType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class StickerServiceImpl implements StickerService{

    /**
     * 스티커 발급
     */
    @Transactional
    @Override
    public Sticker issue(StickerType stickerType, Long principalId) {
        return null;
    }
}
