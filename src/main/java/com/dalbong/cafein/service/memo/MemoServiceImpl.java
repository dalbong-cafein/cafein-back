package com.dalbong.cafein.service.memo;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.domain.memo.*;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.review.ReviewRepository;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.memo.MemoRegDto;
import com.dalbong.cafein.handler.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class MemoServiceImpl implements MemoService{

    private final MemoRepository memoRepository;
    private final StoreMemoRepository storeMemoRepository;
    private final ReviewMemoRepository reviewMemoRepository;
    private final MemberMemoRepository memberMemoRepository;
    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    /**
     * 관리자단 카페 메모 생성
     */
    @Transactional
    @Override
    public Memo register(MemoRegDto memoRegDto, Long principalId) {

        if (memoRegDto.getStoreId() != null) {
            Store store = storeRepository.findById(memoRegDto.getStoreId()).orElseThrow(() ->
                    new CustomException("존재하지 않는 카페입니다."));

            StoreMemo storeMemo = new StoreMemo(store, Member.builder().memberId(principalId).build(), memoRegDto.getContent());

            return storeMemoRepository.save(storeMemo);

        } else if (memoRegDto.getReviewId() != null) {
            Review review = reviewRepository.findById(memoRegDto.getReviewId()).orElseThrow(() ->
                    new CustomException("존재하지 않는 리뷰입니다."));

            ReviewMemo reviewMemo = new ReviewMemo(review, Member.builder().memberId(principalId).build(), memoRegDto.getContent());

            return reviewMemoRepository.save(reviewMemo);

        } else if (memoRegDto.getMemberId() != null) {
            Member member = memberRepository.findById(memoRegDto.getMemberId()).orElseThrow(() ->
                    new CustomException("존재하지 않는 회원입니다."));

            MemberMemo memberMemo = new MemberMemo(member, Member.builder().memberId(principalId).build(), memoRegDto.getContent());
            return memberMemoRepository.save(memberMemo);
        }

        throw new CustomException("존재하는 메모 기능이 없습니다.");
    }
}
