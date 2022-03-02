package com.dalbong.cafein.service.review;

import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.review.ReviewRepository;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.review.ReviewRegDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;


    /**
     * 리뷰 등록
     */
    @Transactional
    @Override
    public Review register(ReviewRegDto reviewRegDto, Long principalId) {

        Store store = storeRepository.findById(reviewRegDto.getStoreId()).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 가게입니다."));

        Review review = reviewRegDto.toEntity(principalId, store);

        reviewRepository.save(review);

        return review;
    }
}
