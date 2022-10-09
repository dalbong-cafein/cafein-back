package com.dalbong.cafein.service.sticker;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.sticker.Sticker;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.admin.sticker.AdminStickerResDto;
import com.dalbong.cafein.dto.sticker.StickerHistoryResDto;

import java.util.List;

public interface StickerService {


    boolean checkPossibleIssueCongestionSticker(Long storeId, Long principalId);

    Sticker issueStoreSticker(Long storeId, Long principalId);

    Sticker issueReviewSticker(Long reviewId, Long principalId);

    Sticker issueCongestionSticker(Long congestionId, Long principalId);

    void recover(Long stickerId);

    void removeStickerList(int limit, Long principalId);

    int countStickerOfMember(Long principalId);

    List<StickerHistoryResDto> getStickerHistoryList(Long principalId);

    List<AdminStickerResDto> getStickerListOfAdmin(Long memberId);

}
