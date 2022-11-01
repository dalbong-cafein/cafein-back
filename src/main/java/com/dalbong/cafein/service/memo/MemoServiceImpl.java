package com.dalbong.cafein.service.memo;

import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.domain.coupon.CouponRepository;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.domain.memo.*;
import com.dalbong.cafein.domain.notice.CouponNoticeRepository;
import com.dalbong.cafein.domain.report.Report;
import com.dalbong.cafein.domain.report.ReportRepository;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.review.ReviewRepository;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.admin.memo.AdminMemoRegDto;
import com.dalbong.cafein.dto.admin.memo.AdminMemoResDto;
import com.dalbong.cafein.dto.admin.memo.AdminMemoUpdateDto;
import com.dalbong.cafein.handler.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class MemoServiceImpl implements MemoService{

    private final MemoRepository memoRepository;
    private final StoreMemoRepository storeMemoRepository;
    private final ReviewMemoRepository reviewMemoRepository;
    private final MemberMemoRepository memberMemoRepository;
    private final CouponMemoRepository couponMemoRepository;
    private final ReportMemoRepository reportMemoRepository;
    private final StoreRepository storeRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final CouponRepository couponRepository;
    private final ReportRepository reportRepository;

    /**
     * 관리자단 메모 생성
     */
    @Transactional
    @Override
    public Memo register(AdminMemoRegDto adminMemoRegDto, Long principalId) {

        //카페 메모
        if (adminMemoRegDto.getStoreId() != null) {
            Store store = storeRepository.findById(adminMemoRegDto.getStoreId()).orElseThrow(() ->
                    new CustomException("존재하지 않는 카페입니다."));

            StoreMemo storeMemo = new StoreMemo(store, Member.builder().memberId(principalId).build(), adminMemoRegDto.getContent());

            return storeMemoRepository.save(storeMemo);

        }
        //리뷰 메모
        else if (adminMemoRegDto.getReviewId() != null) {
            Review review = reviewRepository.findById(adminMemoRegDto.getReviewId()).orElseThrow(() ->
                    new CustomException("존재하지 않는 리뷰입니다."));

            ReviewMemo reviewMemo = new ReviewMemo(review, Member.builder().memberId(principalId).build(), adminMemoRegDto.getContent());

            return reviewMemoRepository.save(reviewMemo);

        }
        //회원 메모
        else if (adminMemoRegDto.getMemberId() != null) {
            Member member = memberRepository.findById(adminMemoRegDto.getMemberId()).orElseThrow(() ->
                    new CustomException("존재하지 않는 회원입니다."));

            MemberMemo memberMemo = new MemberMemo(member, Member.builder().memberId(principalId).build(), adminMemoRegDto.getContent());
            return memberMemoRepository.save(memberMemo);
        }

        //쿠폰 메모
        else if (adminMemoRegDto.getCouponId() != null) {
            Coupon coupon = couponRepository.findById(adminMemoRegDto.getCouponId()).orElseThrow(() ->
                    new CustomException("존재하지 않는 쿠폰입니다."));

            CouponMemo couponMemo = new CouponMemo(coupon, Member.builder().memberId(principalId).build(), adminMemoRegDto.getContent());
            return couponMemoRepository.save(couponMemo);
        }

        //신고 메모
        else if (adminMemoRegDto.getReportId() != null){
            Report report = reportRepository.findById(adminMemoRegDto.getReportId()).orElseThrow(() ->
                    new CustomException("존재하지 않는 신고입니다."));

            ReportMemo reportMemo = new ReportMemo(report, Member.builder().memberId(principalId).build(), adminMemoRegDto.getContent());
            return reportMemoRepository.save(reportMemo);
        }

        throw new CustomException("존재하는 메모 기능이 없습니다.");
    }

    /**
     * 관리자단 메모 수정
     */
    @Transactional
    @Override
    public void modify(AdminMemoUpdateDto adminMemoUpdateDto) {

        Memo memo = memoRepository.findById(adminMemoUpdateDto.getMemoId()).orElseThrow(() ->
                new CustomException("존재하지 않는 메모입니다."));

        memo.changeContent(adminMemoUpdateDto.getContent());
    }

    /**
     * 관리자단 메모 삭제
     */
    @Transactional
    @Override
    public void remove(Long memoId) {

        memoRepository.deleteById(memoId);
    }

    /**
     * 관리자단 최근 생성 or 수정된 메모 리스트 개수 지정 조회
     */
    @Transactional(readOnly = true)
    @Override
    public List<AdminMemoResDto> getCustomLimitMemoList(int limit) {

        List<Memo> result = memoRepository.getCustomLimitMemoList(limit);

        return result.stream().map(m -> new AdminMemoResDto(m)).collect(Collectors.toList());
    }

    /**
     * 관리자단 메모 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminMemoResDto getMemo(Long memoId) {

        Memo memo = memoRepository.findById(memoId).orElseThrow(() ->
                new CustomException("존재하지 않는 메모입니다."));

        return new AdminMemoResDto(memo);
    }
}
