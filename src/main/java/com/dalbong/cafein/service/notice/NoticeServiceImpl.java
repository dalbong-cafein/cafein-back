package com.dalbong.cafein.service.notice;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.domain.image.ReviewImage;
import com.dalbong.cafein.domain.image.StoreImage;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.notice.*;
import com.dalbong.cafein.domain.notice.detailReportNotice.DetailReportNotice;
import com.dalbong.cafein.domain.notice.detailReportNotice.DetailReportNoticeRepository;
import com.dalbong.cafein.domain.report.report.Report;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.review.ReviewRepository;
import com.dalbong.cafein.domain.sticker.Sticker;
import com.dalbong.cafein.dto.image.ImageDto;
import com.dalbong.cafein.dto.notice.NoticeResDto;
import com.dalbong.cafein.dto.notice.detailReportNotice.DetailReportNoticeResDto;
import com.dalbong.cafein.dto.notice.detailReportNotice.ReportedReviewResDto;
import com.dalbong.cafein.handler.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService{

    private final NoticeRepository noticeRepository;
    private final StickerNoticeRepository stickerNoticeRepository;
    private final CouponNoticeRepository couponNoticeRepository;
    private final BoardNoticeRepository boardNoticeRepository;
    private final ReportNoticeRepository reportNoticeRepository;
    private final ReviewRepository reviewRepository;
    private final DetailReportNoticeRepository detailReportNoticeRepository;

    /**
     * 스티커 지급 알림 등록
     */
    @Transactional
    @Override
    public Notice registerStickerNotice(Sticker sticker, Member toMember) {

        StickerNotice stickerNotice = new StickerNotice(sticker, toMember);

        return stickerNoticeRepository.save(stickerNotice);
    }

    /**
     * 쿠폰 지급 알림 등록
     */
    @Transactional
    @Override
    public Notice registerCouponNotice(Coupon coupon, Member toMember) {

        CouponNotice couponNotice = new CouponNotice(coupon, toMember);

        return couponNoticeRepository.save(couponNotice);
    }

    /**
     * 신고 알림 등록
     */
    @Transactional
    @Override
    public Notice registerReportNotice(Report report, Member toMember, int reportCnt) {


        LocalDateTime reportExpiredDateTime = toMember.getReportExpiredDateTime();


        DetailReportNotice detailReportNotice = DetailReportNotice.builder()
                .reportExpiredDateTime(reportExpiredDateTime)
                .stopPostDateTime(LocalDateTime.now())
                .build();

        //신고 텍스트
        String reportText = "작성한 리뷰에 대해 신고가 접수되어 안내드립니다.";
        if(reportExpiredDateTime != null){

            //활동 제한 기한
            int year = reportExpiredDateTime.getYear();
            int month = reportExpiredDateTime.getMonthValue();
            int day = reportExpiredDateTime.getDayOfMonth()-1;

            reportText = "작성한 리뷰에 대해 신고가 접수되어 카페인 활동이 제한되었습니다. " +
                    "신고 정책에 따라 하루 동안 카페 리뷰 작성, 혼잡도 공유 활동이 제한되며 " +
                    "신고된 리뷰는 게시중단 처리됩니다."+System.lineSeparator()+
                    "*활동 제한 기한: ~ "+year+"년 "+month+"월 "+day+"일까지";
        }

        ReportNotice reportNotice;

        switch (reportCnt) {
            case 0:
                reportNotice = reportNoticeRepository.save(new ReportNotice(report, toMember,
                        "[신고 1회] " + reportText, detailReportNotice));
                break;
            case 1:
                reportNotice = reportNoticeRepository.save(new ReportNotice(report, toMember,
                        "[신고 2회] " + reportText, detailReportNotice));
                break;
            case 2:
                reportNotice = reportNoticeRepository.save(new ReportNotice(report, toMember,
                        "[신고 3회] " + reportText, detailReportNotice));
                break;
            case 3:
                reportNotice = reportNoticeRepository.save(new ReportNotice(report, toMember,
                        "[신고 4회] " + reportText, detailReportNotice));
                break;
            default:
                reportCnt += 1;
                reportNotice = reportNoticeRepository.save(new ReportNotice(report, toMember,
                        "[신고 " + reportCnt + "회] " + reportText, detailReportNotice));
        }

        return reportNotice;
    }

    /**
     * 공지사항 알림 등록
     */
    @Transactional
    @Override
    public void registerBoardNotice(Board board , List<Member> toMemberList) {

        List<BoardNotice> boardNoticeList =
                toMemberList.stream().map(m -> new BoardNotice(board, m))
                        .collect(Collectors.toList());

        boardNoticeRepository.saveAll(boardNoticeList);
    }

    /**
     * 알림 읽음 처리
     */
    @Transactional
    @Override
    public void read(Long noticeId) {

        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() ->
                new CustomException("존재하지 않는 알림입니다."));

        notice.read();
    }

    /**
     * 알림 삭제 - by noticeId
     */
    @Transactional
    @Override
    public void remove(Long noticeId) {

        noticeRepository.deleteById(noticeId);
    }

    /**
     * 알림 삭제 - By report
     */
    @Transactional
    @Override
    public void remove(Report report) {

        reportNoticeRepository.deleteByReport(report);
    }

    /**
     * 회원별 전체 알림 삭제
     */
    @Transactional
    @Override
    public void removeAll(Long principalId) {

        List<DetailReportNotice> detailReportNoticeList = reportNoticeRepository.getDetailReportNoticeByMemberId(principalId);

        noticeRepository.deleteByMemberId(principalId);

        detailReportNoticeRepository.deleteInBatch(detailReportNoticeList);
    }

    /**
     * 알림 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public List<NoticeResDto> getNoticeList(Long principalId) {

        List<Notice> results = noticeRepository.getNoticeList(principalId);

        return results.stream().map(n -> new NoticeResDto(n)).collect(Collectors.toList());
    }

    /**
     * 신고 알림 상세 조회
     */
    @Transactional(readOnly = true)
    @Override
    public DetailReportNoticeResDto getDetailReportNotice(Long noticeId) {

        ReportNotice reportNotice = reportNoticeRepository.getDetailReportNotice(noticeId).orElseThrow(() ->
                new CustomException("존재하지 않은 신고 알림입니다."));

        Review review = reviewRepository.findByIdStoreFetch(reportNotice.getReport().getReview().getReviewId())
                .orElseThrow(() -> new CustomException("존재하지 않는 리뷰입니다."));

        //review 이미지 리스트
        List<ImageDto> reviewImageDtoList = getReviewImageDtoList(review);

        //store 이미지
        ImageDto storeImageDto = null;

        if (review.getStore().getStoreImageList() != null && !review.getStore().getStoreImageList().isEmpty()) {
            StoreImage storeImage = review.getStore().getStoreImageList().get(0);
            storeImageDto = new ImageDto(storeImage.getImageId(), storeImage.getImageUrl());
        }

        ReportedReviewResDto reportedReviewResDto = new ReportedReviewResDto(review, reviewImageDtoList,
                storeImageDto, reportNotice.getDetailReportNotice().getStopPostDateTime());

        return new DetailReportNoticeResDto(reportNotice, reportedReviewResDto);
    }

    private List<ImageDto> getReviewImageDtoList(Review review) {

        List<ImageDto> reviewImageDtoList = new ArrayList<>();

        if (review.getReviewImageList() != null && !review.getReviewImageList().isEmpty()) {
            for (ReviewImage reviewImage : review.getReviewImageList()) {
                reviewImageDtoList.add(
                        new ImageDto(reviewImage.getImageId(), reviewImage.getImageUrl(), review.getMember().getNickname()));
            }
        }

        return reviewImageDtoList;
    }
}
