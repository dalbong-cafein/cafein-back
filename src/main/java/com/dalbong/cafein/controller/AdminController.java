package com.dalbong.cafein.controller;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.admin.board.AdminBoardListResDto;
import com.dalbong.cafein.dto.admin.board.AdminBoardRegDto;
import com.dalbong.cafein.dto.admin.coupon.AdminCouponListResDto;
import com.dalbong.cafein.dto.admin.report.AdminReportListResDto;
import com.dalbong.cafein.dto.admin.review.AdminDetailReviewResDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewListResDto;
import com.dalbong.cafein.dto.admin.store.AdminStoreListDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.report.ReportRegDto;
import com.dalbong.cafein.dto.store.StoreRegDto;
import com.dalbong.cafein.service.board.BoardService;
import com.dalbong.cafein.service.coupon.CouponService;
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

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final ReviewService reviewService;
    private final StoreService storeService;
    private final CouponService couponService;
    private final BoardService boardService;
    private final ReportService reportService;

    /**
     * 관리자단 리뷰 리스트 조회
     */
    @GetMapping("/reviews")
    public ResponseEntity<?> getAllReviewList(PageRequestDto requestDto){

        AdminReviewListResDto adminReviewListResDto = reviewService.getReviewListOfAdmin(requestDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 리뷰 리스트 조회 성공", adminReviewListResDto), HttpStatus.OK);
    }

    /**
     * 리뷰 상세 정보 조회
     */
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<?> getDetailReview(@PathVariable("reviewId") Long reviewId){

        AdminDetailReviewResDto adminDetailReviewResDto= reviewService.getDetailReviewOfAdmin(reviewId);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 리뷰 상세 정보 조회 성공", adminDetailReviewResDto), HttpStatus.OK);
    }

    /**
     * 관리자단 가게 등록
     */
    @PostMapping("/stores")
    public ResponseEntity<?> register(@Validated StoreRegDto storeRegDto, BindingResult bindingResult,
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
     * 관리자단 쿠폰 리스트 조회
     */
    @GetMapping("/coupons")
    public ResponseEntity<?> getAllCouponList(PageRequestDto requestDto){

        AdminCouponListResDto adminCouponListResDto = couponService.getCouponListOfAdmin(requestDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 쿠폰 리스트 조회 성공", adminCouponListResDto), HttpStatus.OK);
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
    public ResponseEntity<?> remove(@PathVariable("boardId") Long boardId){

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

        return new ResponseEntity<>(new CMRespDto<>(1, "회원별 신고내역 조회 성공", adminReportListResDto), HttpStatus.OK);
    }
}
