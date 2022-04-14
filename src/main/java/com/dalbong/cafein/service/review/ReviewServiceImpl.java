package com.dalbong.cafein.service.review;

import com.dalbong.cafein.domain.image.MemberImage;
import com.dalbong.cafein.domain.image.ReviewImage;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.review.ReviewRepository;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.image.ImageDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.page.ScrollResultDto;
import com.dalbong.cafein.dto.review.ReviewListDto;
import com.dalbong.cafein.dto.review.ReviewRegDto;
import com.dalbong.cafein.dto.review.ReviewResDto;
import com.dalbong.cafein.dto.review.ReviewUpdateDto;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Transactional
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final ImageService imageService;


    /**
     * 리뷰 등록
     */
    @Transactional
    @Override
    public Review register(ReviewRegDto reviewRegDto, Long principalId) throws IOException {

        Store store = storeRepository.findById(reviewRegDto.getStoreId()).orElseThrow(() ->
                new CustomException("존재하지 않는 매장입니다."));

        Review review = reviewRegDto.toEntity(principalId, store);

        reviewRepository.save(review);

        imageService.saveReviewImage(review, reviewRegDto.getImageFiles());

        return review;
    }

    /**
     * 리뷰 수정
     */
    @Transactional
    @Override
    public void modify(ReviewUpdateDto reviewUpdateDto) throws IOException {

        Review review = reviewRepository.findById(reviewUpdateDto.getReviewId()).orElseThrow(() ->
                new CustomException("존재하는 리뷰가 없습니다."));


        //리뷰 수정 기간 체크
        if (review.getRegDateTime().isBefore(LocalDateTime.now().minusDays(3))){
            throw new CustomException("리뷰 수정기간이 지났습니다.");
        }

        review.changeContent(reviewUpdateDto.getContent());
        review.changeRecommendation(reviewUpdateDto.getRecommendation());
        review.changeDetailEvaluation(reviewUpdateDto.getDetailEvaluation());


        //리뷰 이미지 갱신
        updateReviewImage(review, reviewUpdateDto.getImageFiles(), reviewUpdateDto.getDeleteImageIdList());


    }

    private void updateReviewImage(Review review, List<MultipartFile> updateImageFiles, List<Long> deleteImageIdList) throws IOException {

        //이미지 추가
        imageService.saveReviewImage(review, updateImageFiles);

        //이미지 삭제
        if (deleteImageIdList != null && !deleteImageIdList.isEmpty()){
            for (Long imageId : deleteImageIdList){
                imageService.remove(imageId);
            }
        }
    }

    /**
     * 리뷰 삭제
     */
    @Transactional
    @Override
    public void remove(Long reviewId) {

        Review review = reviewRepository.findById(reviewId).orElseThrow(() ->
                new CustomException("존재하지 않는 리뷰입니다."));

        List<ReviewImage> reviewImageList = review.getReviewImageList();

        //리뷰 이미지 삭제
        for (ReviewImage reviewImage : reviewImageList){
            imageService.remove(reviewImage.getImageId());
        }

        //리뷰 삭제
        reviewRepository.deleteById(reviewId);
    }

    /**
     * 리뷰 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public ReviewListDto getReviewListOfStore(PageRequestDto pageRequestDto, Long storeId) {

        //TODO 동적 필요
        Pageable pageable = pageRequestDto.getPageable(Sort.by("reviewId").descending());

        Page<Object[]> results = reviewRepository.getReviewListOfStore(storeId, pageRequestDto.getIsOnlyImage(), pageable);

        Function<Object[], ReviewResDto> fn = (arr -> {

            //작성자 프로필 이미지
            MemberImage memberImage = (MemberImage) arr[1];

            String profileImageUrl = null;
            if (memberImage != null){
                profileImageUrl = memberImage.getImageUrl();
            }

            //리뷰 이미지
            Review review = (Review) arr[0];
            List<ImageDto> reviewImageDtoList = new ArrayList<>();
            if (review.getReviewImageList() != null && !review.getReviewImageList().isEmpty()){

                for (ReviewImage reviewImage : review.getReviewImageList()){
                    reviewImageDtoList.add(new ImageDto(reviewImage.getImageId(), reviewImage.getImageUrl()));
                }
            }

            return new ReviewResDto(review, profileImageUrl, (long)arr[2], reviewImageDtoList);
        });


        return new ReviewListDto(results.getTotalElements(), new ScrollResultDto<>(results, fn));
    }
}
