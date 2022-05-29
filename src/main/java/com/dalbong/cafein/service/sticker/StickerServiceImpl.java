package com.dalbong.cafein.service.sticker;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.dalbong.cafein.domain.congestion.CongestionRepository;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.review.ReviewRepository;
import com.dalbong.cafein.domain.sticker.*;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.handler.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class StickerServiceImpl implements StickerService{
    
    private final StickerRepositoryImpl stickerRepository;
    private final StoreStickerRepository storeStickerRepository;
    private final ReviewStickerRepository reviewStickerRepository;
    private final CongestionStickerRepository congestionStickerRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    private final CongestionRepository congestionRepository;

    /**
     * 카페 등록 시 스티커 발급
     */
    @Transactional
    @Override
    public Sticker issueStoreSticker(Long storeId, Long principalId) {

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));

        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new CustomException("존재하지 않는 카페입니다."));

        checkLimitStickerToday(principalId);

        StoreSticker storeSticker = new StoreSticker(store, member);

        return storeStickerRepository.save(storeSticker);
    }

    /**
     * 리뷰 등록 시 스티커 발급
     */
    @Transactional
    @Override
    public Sticker issueReviewSticker(Long reviewId, Long principalId) {

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));

        Review review = reviewRepository.findById(reviewId).orElseThrow(() ->
                new CustomException("존재하지 않는 리뷰입니다."));

        checkLimitStickerToday(principalId);

        ReviewSticker reviewSticker = new ReviewSticker(review, member);

        return reviewStickerRepository.save(reviewSticker);
    }

    /**
     * 혼잡도 등록 시 스티커 발급
     */
    @Transactional
    @Override
    public Sticker issueCongestionSticker(Long congestionId, Long principalId) {

        checkLimitStickerToday(principalId);

        Congestion congestion = congestionRepository.findById(congestionId).orElseThrow(() ->
                new CustomException("존재하지 않는 혼잡도입니다."));

        //혼잡도 스티커일 경우 시간 체크 - 3시간 이내 스티커 발급 체크
        checkLimitTimeOfCongestionSticker(congestion, principalId);

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));


        CongestionSticker congestionSticker = new CongestionSticker(congestion, member);

        return congestionStickerRepository.save(congestionSticker);
    }

    private void checkLimitTimeOfCongestionSticker(Congestion congestion, Long principalId) {
        boolean isExist = stickerRepository.existWithinTimeOfCongestionType(congestion, principalId);

        if(isExist){
            throw new CustomException("동일한 카페의 3시간 이내 혼잡도 등록은 스티커 발급할 수 없습니다.");
        }
    }

    private void checkLimitStickerToday(Long principalId) {

        long count = stickerRepository.getCountStickerToday(principalId);

        if(count >= 3){
            throw new CustomException("하루 최대 스티커 수량을 초과하였습니다.");
        }

    }
}
