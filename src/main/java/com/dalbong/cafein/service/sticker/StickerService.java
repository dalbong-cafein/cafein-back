package com.dalbong.cafein.service.sticker;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.sticker.Sticker;
import com.dalbong.cafein.domain.store.Store;

public interface StickerService {

    Sticker issueStoreSticker(Store store, Long principalId);

    Sticker issueReviewSticker(Review reivew, Long principalId);

    Sticker issueCongestionSticker(Congestion congestion, Long principalId);

}
