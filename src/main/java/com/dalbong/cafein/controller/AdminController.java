package com.dalbong.cafein.controller;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.admin.board.AdminBoardListResDto;
import com.dalbong.cafein.dto.admin.board.AdminBoardRegDto;
import com.dalbong.cafein.dto.admin.coupon.AdminCouponListResDto;

import com.dalbong.cafein.dto.admin.member.AdminDetailMemberResDto;
import com.dalbong.cafein.dto.admin.member.AdminMemberListResDto;
import com.dalbong.cafein.dto.admin.memo.AdminMemoResDto;
import com.dalbong.cafein.dto.admin.report.AdminReportListResDto;
import com.dalbong.cafein.dto.admin.review.AdminDetailReviewResDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewEvaluationOfStoreResDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewListResDto;
import com.dalbong.cafein.dto.admin.store.AdminDetailStoreResDto;
import com.dalbong.cafein.dto.admin.store.AdminStoreListDto;
import com.dalbong.cafein.dto.admin.memo.AdminMemoRegDto;
import com.dalbong.cafein.dto.admin.memo.AdminMemoUpdateDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.store.StoreRegDto;
import com.dalbong.cafein.service.board.BoardService;
import com.dalbong.cafein.service.coupon.CouponService;
import com.dalbong.cafein.service.member.MemberService;
import com.dalbong.cafein.service.memo.MemoService;
import com.dalbong.cafein.service.report.ReportService;
import com.dalbong.cafein.service.review.ReviewService;
import com.dalbong.cafein.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final ReviewService reviewService;
    private final StoreService storeService;
    private final CouponService couponService;
    private final BoardService boardService;
    private final ReportService reportService;
    private final MemberService memberService;
    private final MemoService memoService;

    /**
     * 관리자단 리뷰 리스트 조회
     */
    @GetMapping("/reviews")
    public ResponseEntity<?> getAllReviewList(PageRequestDto requestDto){

        AdminReviewListResDto adminReviewListResDto = reviewService.getReviewListOfAdmin(requestDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 리뷰 리스트 조회 성공", adminReviewListResDto), HttpStatus.OK);
    }

    /**
     * 관리자단 리뷰 상세 정보 조회
     */
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<?> getDetailReview(@PathVariable("reviewId") Long reviewId){

        AdminDetailReviewResDto adminDetailReviewResDto= reviewService.getDetailReviewOfAdmin(reviewId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 리뷰 상세 정보 조회 성공", adminDetailReviewResDto), HttpStatus.OK);
    }

    /**
     * 관리자단 카페 리뷰 상세 평가 정보 조회
     */
    @GetMapping("/stores/{storeId}/reviews/detail-evaluation")
    public ResponseEntity<?> getReviewDetailEvaluationOfStore(@PathVariable("storeId") Long storeId){

        AdminReviewEvaluationOfStoreResDto adminReviewEvaluationOfStoreResDto =
                reviewService.getReviewDetailEvaluationOfStore(storeId);

        return new ResponseEntity<>(new CMRespDto<>(
                1, "관리자단 카페 리뷰 상세 평가 정보 조회 성공", adminReviewEvaluationOfStoreResDto), HttpStatus.OK);
    }

    /**
     * 관리자단 가게 등록
     */
    @PostMapping("/stores")
    public ResponseEntity<?> registerStore(@Validated StoreRegDto storeRegDto, BindingResult bindingResult,
                                      @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        storeService.register(storeRegDto,principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1,"관리자단 가게 등록 성공",null), HttpStatus.CREATED);
    }

    /**
     * 관리자단 가게 리스트 조회
     */
    @GetMapping("/stores")
    public ResponseEntity<?> getAllStoreList(PageRequestDto requestDto){

        AdminStoreListDto adminStoreListDto = storeService.getStoreListOfAdmin(requestDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 카페 리스트 조회 성공", adminStoreListDto), HttpStatus.OK);
    }

    /**
     * 관리자단 가게 상세 조회
     */
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<?> getDetailStore(@PathVariable("storeId") Long storeId){

        AdminDetailStoreResDto adminDetailStoreResDto = storeService.getDetailStoreOfAdmin(storeId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 카페 상세 조회 성공", adminDetailStoreResDto), HttpStatus.OK);
    }

    /**
     * 관리자단 쿠폰 리스트 조회
     */
    @GetMapping("/coupons")
    public ResponseEntity<?> getAllCouponList(PageRequestDto requestDto){

        AdminCouponListResDto adminCouponListResDto = couponService.getCouponListOfAdmin(requestDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 쿠폰 리스트 조회 성공", adminCouponListResDto), HttpStatus.OK);
    }

    /**
     * 쿠폰 상태 변경
     */
    @PatchMapping("/coupons/{couponId}")
    public ResponseEntity<?> issue(@PathVariable("couponId") Long couponId){

        couponService.issue(couponId);

        return new ResponseEntity<>(new CMRespDto<>(1, "쿠폰 상태 변경 성공", null), HttpStatus.OK);
    }

    /**
     * 관리자단 게시글 등록
     */
    @PostMapping("/boards")
    public ResponseEntity<?> registerBoard(AdminBoardRegDto adminBoardRegDto, @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        boardService.register(adminBoardRegDto, principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "게시글 등록 성공", null), HttpStatus.CREATED);
    }

    /**
     * 관리자단 게시글 삭제
     */
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<?> removeBoard(@PathVariable("boardId") Long boardId){

        boardService.remove(boardId);

        return new ResponseEntity<>(new CMRespDto<>(1, "게시글 삭제 성공",null), HttpStatus.OK);
    }

    /**
     * 관리자단 게시글 리스트 조회
     */
    @GetMapping("/boards")
    public ResponseEntity<?> getBoardList(@RequestParam(value = "boardCategoryId", defaultValue = "1", required = false) Long boardCategoryId,
                                             PageRequestDto requestDto){

        AdminBoardListResDto adminBoardListResDto = boardService.getBoardListOfAdmin(boardCategoryId, requestDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 게시글 리스트 조회 성공", adminBoardListResDto), HttpStatus.OK);
    }

    /**
     * 관리자단 회원별 신고내역 조회
     */
    @GetMapping("/members/{memberId}/reports")
    public ResponseEntity<?> getReportList(@PathVariable("memberId") Long memberId){

        AdminReportListResDto adminReportListResDto = reportService.getReportListOfAdmin(memberId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 회원별 신고내역 조회 성공", adminReportListResDto), HttpStatus.OK);
    }

    /**
     * 관리자단 회원 리스트 조회
     */
    @GetMapping("/members")
    public ResponseEntity<?> getMemberList(PageRequestDto requestDto){

        AdminMemberListResDto adminMemberListResDto = memberService.getMemberListOfAdmin(requestDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 회원 리스트 조회 성공", adminMemberListResDto), HttpStatus.OK);
    }

    /**
     * 관리자단 회원 상세 조회
     */
    @GetMapping("/members/{memberId}")
    public ResponseEntity<?> getDetailMember(@PathVariable("memberId") Long memberId){

        AdminDetailMemberResDto adminDetailMemberResDto = memberService.getDetailMemberOfAdmin(memberId);

        return new ResponseEntity<>(
                new CMRespDto<>(1, "관리자단 상세 회원 조회 성공", adminDetailMemberResDto), HttpStatus.OK);
    }

    /**
     * 관리자단 메모 생성
     */
    @PostMapping("/memos")
    public ResponseEntity<?> registerMemo(@RequestBody AdminMemoRegDto adminMemoRegDto, @AuthenticationPrincipal PrincipalDetails principalDetails){

        memoService.register(adminMemoRegDto, principalDetails.getMember().getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 메모 생성 성공", null), HttpStatus.CREATED);
    }

    /**
     * 관리자단 메모 수정
     */
    @PutMapping("/memos/{memoId}")
    public ResponseEntity<?> modifyMemo(@RequestBody AdminMemoUpdateDto adminMemoUpdateDto){

        memoService.modify(adminMemoUpdateDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 메모 수정 성공", null), HttpStatus.OK);
    }

    /**
     * 관리자단 메모 삭제
     */
    @DeleteMapping("/memos/{memoId}")
    public ResponseEntity<?> removeMemo(@PathVariable("memoId") Long memoId){

        memoService.remove(memoId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 메모 삭제 성공", null), HttpStatus.OK);
    }

    /**
     * 관리자단 최근 생성 or 수정된 메모 리스트 개수 지정 조회
     */
    @GetMapping("memos/recent")
    public ResponseEntity<?> getRecentMemoList(@RequestParam(value = "limit", defaultValue = "6", required = false) int limit){

        List<AdminMemoResDto> adminMemoResDtoList = memoService.getCustomLimitMemoList(limit);

        return new ResponseEntity<>(
                new CMRespDto<>(1, "관리자단 최근 생성 or 수정된 메모 리스트 개수 지정 조회 성공", adminMemoResDtoList), HttpStatus.OK);

    }

    /**
     * 관리자단 메모 조회
     */
    @GetMapping("/memos/{memoId}")
    public ResponseEntity<?> getMemo(@PathVariable("memoId") Long memoId){

        AdminMemoResDto adminMemoResDto = memoService.getMemo(memoId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 메모 조회 성공", adminMemoResDto), HttpStatus.OK);
    }
}
