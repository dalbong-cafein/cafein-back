package com.dalbong.cafein.controller.admin;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.domain.image.Image;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.admin.RegisterDataCountOfTodayDto;
import com.dalbong.cafein.dto.admin.board.AdminBoardListResDto;
import com.dalbong.cafein.dto.admin.board.AdminBoardRegDto;
import com.dalbong.cafein.dto.admin.board.AdminBoardUpdateDto;
import com.dalbong.cafein.dto.admin.congestion.AdminCongestionListResDto;
import com.dalbong.cafein.dto.admin.congestion.AdminCongestionResDto;
import com.dalbong.cafein.dto.admin.coupon.AdminCouponListResDto;

import com.dalbong.cafein.dto.admin.event.AdminEventListResDto;
import com.dalbong.cafein.dto.admin.member.AdminDetailMemberResDto;
import com.dalbong.cafein.dto.admin.member.AdminMemberListResDto;
import com.dalbong.cafein.dto.admin.member.AdminMemberUpdateDto;
import com.dalbong.cafein.dto.admin.memo.AdminMemoResDto;
import com.dalbong.cafein.dto.admin.report.AdminReportListResDto;
import com.dalbong.cafein.dto.admin.report.AdminReportResDto;
import com.dalbong.cafein.dto.admin.review.AdminDetailReviewResDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewEvaluationOfStoreResDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewListResDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewResDto;
import com.dalbong.cafein.dto.admin.sticker.AdminStickerResDto;
import com.dalbong.cafein.dto.admin.store.AdminDetailStoreResDto;
import com.dalbong.cafein.dto.admin.store.AdminMyStoreResDto;
import com.dalbong.cafein.dto.admin.store.AdminStoreListDto;
import com.dalbong.cafein.dto.admin.memo.AdminMemoRegDto;
import com.dalbong.cafein.dto.admin.memo.AdminMemoUpdateDto;
import com.dalbong.cafein.dto.congestion.CongestionListResDto;
import com.dalbong.cafein.dto.congestion.CongestionResDto;
import com.dalbong.cafein.dto.event.EventRegDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.report.ReportRegDto;
import com.dalbong.cafein.dto.reportCategory.ReportCategoryResDto;
import com.dalbong.cafein.dto.store.StoreRegDto;
import com.dalbong.cafein.dto.store.StoreUpdateDto;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.service.board.BoardService;
import com.dalbong.cafein.service.congestion.CongestionService;
import com.dalbong.cafein.service.coupon.CouponService;
import com.dalbong.cafein.service.event.EventService;
import com.dalbong.cafein.service.image.ImageService;
import com.dalbong.cafein.service.member.MemberService;
import com.dalbong.cafein.service.memo.MemoService;
import com.dalbong.cafein.service.report.ReportService;
import com.dalbong.cafein.service.reportCategory.ReportCategoryService;
import com.dalbong.cafein.service.review.ReviewService;
import com.dalbong.cafein.service.sticker.StickerService;
import com.dalbong.cafein.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class AdminController {

    private final ReviewService reviewService;
    private final StoreService storeService;
    private final MemberService memberService;

    /**
     * 관리자단 오늘 등록된 카페, 회원, 리뷰 수 조회
     */
    @GetMapping("/register-data")
    public ResponseEntity<?> getRegisterDateCountOfToday(){

        Long storeCnt = storeService.getRegisterCountOfToday();
        Long memberCnt = memberService.getRegisterCountOfToday();
        Long reviewCnt = reviewService.getRegisterCountOfToday();

        RegisterDataCountOfTodayDto registerDataCountOfTodayDto = new RegisterDataCountOfTodayDto(storeCnt, memberCnt, reviewCnt);

        return new ResponseEntity<>(new CMRespDto<>(1,"오늘 등록된 카페, 회원, 리뷰 수 조회", registerDataCountOfTodayDto), HttpStatus.OK);
    }
}
