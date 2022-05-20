package com.dalbong.cafein.service.sticker;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.sticker.*;
import com.dalbong.cafein.domain.store.Store;
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

    /**
     * 카페 등록 시 스티커 발급
     */
    @Transactional
    @Override
    public Sticker issueStoreSticker(Store store, Long principalId) {



        return null;
    }

    /**
     * 리뷰 등록 시 스티커 발급
     */
    @Transactional
    @Override
    public Sticker issueReviewSticker(Review review, Long principalId) {



        return null;
    }


    /**
     * 혼잡도 등록 시 스티커 발급
     */
    @Transactional
    @Override
    public Sticker issueCongestionSticker(Congestion congestion, Long principalId) {

        //혼잡도 스티커일 경우 시간 체크 - 3시간 이내 스티커 발급 체크
        boolean isExist = stickerRepository.existWithinTimeOfCongestionType(principalId);

        if(isExist){
            throw new CustomException("동일한 카페의 3시간 이내 혼잡도 등록은 스티커 발급할 수 없습니다.");
        }

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하는 않는 회원입니다."));


        CongestionSticker congestionSticker = new CongestionSticker(congestion, member);

        return congestionStickerRepository.save(congestionSticker);
    }
}
