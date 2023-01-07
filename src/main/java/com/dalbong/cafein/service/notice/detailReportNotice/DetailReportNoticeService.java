package com.dalbong.cafein.service.notice.detailReportNotice;

import com.dalbong.cafein.domain.image.ReviewImage;
import com.dalbong.cafein.domain.image.StoreImage;
import com.dalbong.cafein.domain.notice.Notice;
import com.dalbong.cafein.domain.notice.ReportNotice;
import com.dalbong.cafein.domain.notice.detailReportNotice.DetailReportNotice;
import com.dalbong.cafein.domain.notice.detailReportNotice.DetailReportNoticeRepository;
import com.dalbong.cafein.domain.report.report.ReportRepository;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.review.ReviewRepository;
import com.dalbong.cafein.dto.image.ImageDto;
import com.dalbong.cafein.dto.notice.detailReportNotice.DetailReportNoticeResDto;
import com.dalbong.cafein.dto.notice.detailReportNotice.ReportedReviewResDto;
import com.dalbong.cafein.handler.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class DetailReportNoticeService {

    private final DetailReportNoticeRepository detailReportNoticeRepository;
    private final ReviewRepository reviewRepository;


    /**
     * 상세 신고 알림 저장
     */
    @Transactional
    public DetailReportNotice register(ReportNotice reportNotice, LocalDateTime reportExpiredDateTime, LocalDateTime stopPostDateTime){

        DetailReportNotice detailReportNotice = DetailReportNotice.builder()
                .reportExpiredDateTime(reportExpiredDateTime)
                .stopPostDateTime(stopPostDateTime)
                .build();

        detailReportNotice.setReportNotice(reportNotice);

        return detailReportNoticeRepository.save(detailReportNotice);
    }

    /**
     * 신고 알림 상세 조회
     */
    @Transactional
    public DetailReportNoticeResDto getDetailReportNotice(Long noticeId){

        DetailReportNotice detailReportNotice = detailReportNoticeRepository.getDetailReportNotice(noticeId);

        //신고 타입의 알림이 아닐 경우
        if(detailReportNotice == null){
            throw new CustomException("해당 알림은 신고 타입이 아닙니다.");
        }

        Review review = reviewRepository.findByIdStoreFetch(detailReportNotice.getReportNotice().getReport().getReview().getReviewId())
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
                storeImageDto, detailReportNotice.getStopPostDateTime());


        return new DetailReportNoticeResDto(detailReportNotice, reportedReviewResDto);
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
