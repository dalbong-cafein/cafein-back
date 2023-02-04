package com.dalbong.cafein.service.sticker;

import com.dalbong.cafein.domain.congestion.Congestion;
import com.dalbong.cafein.domain.congestion.CongestionRepository;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.domain.notice.StickerNoticeRepository;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.review.ReviewRepository;
import com.dalbong.cafein.domain.sticker.*;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.admin.sticker.AdminStickerResDto;
import com.dalbong.cafein.dto.sticker.PossibleIssueResDto;
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

//    /**
//     * 스티커 발급 가능 여부 체크
//     */
//    @Transactional(readOnly = true)
//    @Override
//    public PossibleIssueResDto checkPossibleIssue(Long principalId) {
//
//        //회원의 보유 스티커 개수 체크
//        int stickerCnt = countStickerOfMember(principalId);
//
//        if(stickerCnt >= 20){
//            return new PossibleIssueResDto(false, "보유 가능한 스티커 수량 초과");
//        }
//
//        //하루동안 발급 받은 스티커 개수 체크
//        long count = stickerRepository.getCountStickerToday(principalId);
//
//        if(count >= 3){
//            return new PossibleIssueResDto(false, "하루 최대 스티커 발급 수량 초과");
//        }
//
//        return new PossibleIssueResDto(true, null);
//    }

    /**
     * 스티커 발급 가능 여부 체크
     */
    @Transactional(readOnly = true)
    @Override
    public PossibleIssueResDto checkPossibleIssue(Long principalId) {

        return new PossibleIssueResDto(false, "스티커 기능이 중지되었습니다.");
    }

    /**
     * 카페 등록 시 스티커 발급
     */
    @Transactional
    @Override
    public Sticker issueStoreSticker(Long storeId, Long principalId) {

        //스티커 기능 중지
        if(principalId != null){
            throw new StickerExcessException("스티커 기능이 중지되었습니다.");
        }

        //회원의 스티커 개수 체크
        checkLimitNumberOfSticker(principalId);

        //하루동안 발급 받은 스티커 개수 체크
        checkLimitStickerToday(principalId);

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));

        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new CustomException("존재하지 않는 카페입니다."));

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

        //스티커 기능 중지
        if(principalId != null){
            throw new StickerExcessException("스티커 기능이 중지되었습니다.");
        }

        //회원의 스티커 개수 체크
        checkLimitNumberOfSticker(principalId);

        //하루동안 발급 받은 스티커 개수 체크
        checkLimitStickerToday(principalId);

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));

        Review review = reviewRepository.findByIdStoreFetch(reviewId).orElseThrow(() ->
                new CustomException("존재하지 않는 리뷰입니다."));

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

        //스티커 기능 중지
        if(principalId != null){
            throw new StickerExcessException("스티커 기능이 중지되었습니다.");
        }

        //회원의 스티커 개수 체크
        checkLimitNumberOfSticker(principalId);

        //하루동안 발급 받은 스티커 개수 체크
        checkLimitStickerToday(principalId);

        Congestion congestion = congestionRepository.findByIdStoreFetch(congestionId).orElseThrow(() ->
                new CustomException("존재하지 않는 혼잡도입니다."));

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));


        CongestionSticker congestionSticker = new CongestionSticker(congestion, member);

        congestionStickerRepository.save(congestionSticker);

        //스티커 발급 알림 등록
        noticeService.registerStickerNotice(congestionSticker, member);

        return congestionSticker;
    }

    /**
     * 스티커 회수
     */
    @Transactional
    @Override
    public void recover(Long stickerId) {

        Sticker sticker = stickerRepository.findById(stickerId).orElseThrow(() ->
                new CustomException("존재하지 않는 스티커입니다."));

        //스티커 알림 삭제
        stickerNoticeRepository.deleteBySticker(sticker);

        stickerRepository.deleteById(stickerId);
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
        return stickerRepository.getCountStickerOfMember(principalId);

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

    private void checkLimitNumberOfSticker(Long principalId) {

        int stickerCnt = countStickerOfMember(principalId);

        if(stickerCnt >= 20){
            throw new StickerExcessException("보유 가능한 스티커 수량 초과");
        }
    }

    private void checkLimitStickerToday(Long principalId) {

        long count = stickerRepository.getCountStickerToday(principalId);

        if(count >= 3){
            throw new StickerExcessException("하루 최대 스티커 발급 수량 초과");
        }

    }
}
