package com.dalbong.cafein.service.sticker;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.dalbong.cafein.domain.congestion.CongestionRepository;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.domain.notice.NoticeRepository;
import com.dalbong.cafein.domain.notice.StickerNoticeRepository;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.review.ReviewRepository;
import com.dalbong.cafein.domain.sticker.*;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.admin.sticker.AdminStickerResDto;
import com.dalbong.cafein.dto.sticker.StickerHistoryResDto;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.handler.exception.StickerExcessException;
import com.dalbong.cafein.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class StickerServiceImpl implements StickerService{

    private final StickerRepository stickerRepository;
    private final StoreStickerRepository storeStickerRepository;
    private final ReviewStickerRepository reviewStickerRepository;
    private final CongestionStickerRepository congestionStickerRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    private final CongestionRepository congestionRepository;
    private final NoticeService noticeService;
    private final StickerNoticeRepository stickerNoticeRepository;

    /**
     * 카페 등록 시 스티커 발급
     */
    @Transactional
    @Override
    public Sticker issueStoreSticker(Long storeId, Long principalId) {

        int stickerCnt = countStickerOfMember(principalId);

        if(stickerCnt >= 20){
            throw new StickerExcessException("더 이상 스티커를 발급받을 수 없습니다.");
        }

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));

        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new CustomException("존재하지 않는 카페입니다."));

        //checkLimitStickerToday(principalId);

        StoreSticker storeSticker = new StoreSticker(store, member);

        storeStickerRepository.save(storeSticker);

        //스티커 발급 알림 등록
        noticeService.registerStickerNotice(storeSticker, member);

        return storeSticker;
    }

    /**
     * 리뷰 등록 시 스티커 발급
     */
    @Transactional
    @Override
    public Sticker issueReviewSticker(Long reviewId, Long principalId) {

        int stickerCnt = countStickerOfMember(principalId);

        if(stickerCnt >= 20){
            throw new StickerExcessException("더 이상 스티커를 발급받을 수 없습니다.");
        }

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));

        Review review = reviewRepository.findByIdStoreFetch(reviewId).orElseThrow(() ->
                new CustomException("존재하지 않는 리뷰입니다."));

        checkLimitStickerToday(principalId);

        ReviewSticker reviewSticker = new ReviewSticker(review, member);

        reviewStickerRepository.save(reviewSticker);

        //스티커 발급 알림 등록
        noticeService.registerStickerNotice(reviewSticker, member);

        return reviewSticker;
    }

    /**
     * 혼잡도 등록 시 스티커 발급
     */
    @Transactional
    @Override
    public Sticker issueCongestionSticker(Long congestionId, Long principalId) {

        int stickerCnt = countStickerOfMember(principalId);

        if(stickerCnt >= 20){
            throw new StickerExcessException("더 이상 스티커를 발급받을 수 없습니다.");
        }

        checkLimitStickerToday(principalId);

        Congestion congestion = congestionRepository.findByIdStoreFetch(congestionId).orElseThrow(() ->
                new CustomException("존재하지 않는 혼잡도입니다."));

        //혼잡도 스티커일 경우 시간 체크 - 3시간 이내 스티커 발급 체크
        checkLimitTimeOfCongestionSticker(congestion, principalId);

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));


        CongestionSticker congestionSticker = new CongestionSticker(congestion, member);

        congestionStickerRepository.save(congestionSticker);

        //스티커 발급 알림 등록
        noticeService.registerStickerNotice(congestionSticker, member);

        return congestionSticker;
    }

    /**
     * 카페 스티커 회수
     */
    @Transactional
    @Override
    public void recoverStoreSticker(Long storeId, Long principalId) {

        Sticker sticker = stickerRepository.findByStoreIdAndMemberId(storeId, principalId).orElseThrow(() ->
                new CustomException("존재하지 않는 스티커입니다."));

        //해당 알림 삭제
        stickerNoticeRepository.deleteBySticker(sticker);

        storeStickerRepository.delete((StoreSticker) sticker);
    }

    /**
     * 리뷰 스티커 회수
     */
    @Transactional
    @Override
    public void recoverReviewSticker(Long reviewId, Long principalId) {

        Sticker sticker = stickerRepository.findByReviewIdAndMemberId(reviewId,principalId).orElseThrow(() ->
                new CustomException("존재하지 않는 스티커입니다."));

        //해당 알림 삭제
        stickerNoticeRepository.deleteBySticker(sticker);

        reviewStickerRepository.delete((ReviewSticker) sticker);

    }

    /**
     * 혼잡도 스티커 회수
     */
    @Transactional
    @Override
    public void recoverCongestionSticker(Long congestionId, Long principalId) {

        Sticker sticker = stickerRepository.findByCongestionIdAndMemberId(congestionId, principalId).orElseThrow(() ->
                new CustomException("존재하지 않는 스티커입니다."));

        //해당 알림 삭제
        stickerNoticeRepository.deleteBySticker(sticker);

        congestionStickerRepository.delete((CongestionSticker) sticker);

    }

    /**
     * 스티커 삭제
     */
    @Transactional
    @Override
    public void removeStickerList(int limit, Long principalId) {

        List<Sticker> results = stickerRepository.getCustomLimitStickerList(limit, principalId);

        for(Sticker s : results){
            //알림 삭제
            stickerNoticeRepository.deleteBySticker(s);
            //스티커 삭제
            stickerRepository.delete(s);
            }
        }


    /**
     * 회원별 스티커 보유 개수 조회
     */
    @Transactional(readOnly = true)
    @Override
    public int countStickerOfMember(Long principalId) {
        return (int)stickerRepository.getCountStickerToday(principalId);

    }

    /**
     * 회원별 스티커 히스토리 내역 조회
     */
    @Transactional(readOnly = true)
    @Override
    public List<StickerHistoryResDto> getStickerHistoryList(Long principalId) {

        List<Sticker> results = stickerRepository.getStickerList(principalId);

        return results.stream().map(s -> new StickerHistoryResDto(s)).collect(Collectors.toList());
    }

    /**
     * 관리자단 회원별 스티커 내역 조회
     */
    @Transactional(readOnly = true)
    @Override
    public List<AdminStickerResDto> getStickerListOfAdmin(Long memberId) {

        List<Sticker> results = stickerRepository.getStickerList(memberId);

        return results.stream().map(s -> new AdminStickerResDto(s)).collect(Collectors.toList());
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
