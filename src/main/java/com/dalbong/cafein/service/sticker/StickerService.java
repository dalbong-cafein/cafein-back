package com.dalbong.cafein.service.sticker;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.sticker.Sticker;
import com.dalbong.cafein.domain.store.Store;

public interface StickerService {

    Sticker issueStoreSticker(Long storeId, Long principalId);

    Sticker issueReviewSticker(Long reviewId, Long principalId);

    Sticker issueCongestionSticker(Long congestionId, Long principalId);

}
