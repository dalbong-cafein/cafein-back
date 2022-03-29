package com.dalbong.cafein.service.review;

import com.dalbong.cafein.domain.image.ReviewImage;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.review.ReviewRepository;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.review.ReviewRegDto;
import com.dalbong.cafein.dto.review.ReviewUpdateDto;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

        review.changeContent(reviewUpdateDto.getContent());
        review.changeRecommendation(reviewUpdateDto.getRecommendation());
        review.changeDetailEvaluation(reviewUpdateDto.getDetailEvaluation());

        //리뷰 이미지 갱신
        updateReviewImage(review, reviewUpdateDto.getImageFiles());


    }

    private void updateReviewImage(Review review, List<MultipartFile> updateImageFiles) throws IOException {

        //리뷰 기존 이미지 삭제
        for (ReviewImage reviewImage : review.getReviewImageList()){
            imageService.remove(reviewImage.getImageId());
        }

        //갱신된 이미지 저장
        imageService.saveReviewImage(review, updateImageFiles);
    }

    /*
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

        reviewRepository.deleteById(reviewId);

    }
}
