package com.dalbong.cafein.service.memo;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.domain.memo.*;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.review.ReviewRepository;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.memo.MemoRegDto;
import com.dalbong.cafein.dto.memo.MemoUpdateDto;
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
     * 관리자단 메모 생성
     */
    @Transactional
    @Override
    public Memo register(MemoRegDto memoRegDto, Long principalId) {

        //카페 메모
        if (memoRegDto.getStoreId() != null) {
            Store store = storeRepository.findById(memoRegDto.getStoreId()).orElseThrow(() ->
                    new CustomException("존재하지 않는 카페입니다."));

            StoreMemo storeMemo = new StoreMemo(store, Member.builder().memberId(principalId).build(), memoRegDto.getContent());

            return storeMemoRepository.save(storeMemo);

        }
        //리뷰 메모
        else if (memoRegDto.getReviewId() != null) {
            Review review = reviewRepository.findById(memoRegDto.getReviewId()).orElseThrow(() ->
                    new CustomException("존재하지 않는 리뷰입니다."));

            ReviewMemo reviewMemo = new ReviewMemo(review, Member.builder().memberId(principalId).build(), memoRegDto.getContent());

            return reviewMemoRepository.save(reviewMemo);

        }
        //회원 메모
        else if (memoRegDto.getMemberId() != null) {
            Member member = memberRepository.findById(memoRegDto.getMemberId()).orElseThrow(() ->
                    new CustomException("존재하지 않는 회원입니다."));

            MemberMemo memberMemo = new MemberMemo(member, Member.builder().memberId(principalId).build(), memoRegDto.getContent());
            return memberMemoRepository.save(memberMemo);
        }

        throw new CustomException("존재하는 메모 기능이 없습니다.");
    }

    /**
     * 관리자단 메모 수정
     */
    @Transactional
    @Override
    public void modify(MemoUpdateDto memoUpdateDto) {

        Memo memo = memoRepository.findById(memoUpdateDto.getMemoId()).orElseThrow(() ->
                new CustomException("존재하지 않는 메모입니다."));

        memo.changeContent(memoUpdateDto.getContent());
    }

    /**
     * 관리자단 메모 삭제
     */
    @Transactional
    @Override
    public void remove(Long memoId) {

        memoRepository.deleteById(memoId);
    }
}
