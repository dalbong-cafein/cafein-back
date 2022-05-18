package com.dalbong.cafein.service.sticker;

import com.dalbong.cafein.domain.sticker.Sticker;
import com.dalbong.cafein.domain.sticker.StickerType;

public interface StickerService {

    Sticker issue(StickerType stickerType, Long principalId);

}
