package com.dalbong.cafein.service.review;

import com.dalbong.cafein.domain.image.MemberImage;
import com.dalbong.cafein.domain.image.ReviewImage;
import com.dalbong.cafein.domain.image.StoreImage;
import com.dalbong.cafein.domain.memo.ReviewMemo;
import com.dalbong.cafein.domain.review.DetailEvaluation;
import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.review.ReviewRepository;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.admin.review.*;
import com.dalbong.cafein.dto.image.ImageDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.page.PageResultDTO;
import com.dalbong.cafein.dto.page.ScrollResultDto;
import com.dalbong.cafein.dto.review.*;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.service.image.ImageService;
import com.dalbong.cafein.service.sticker.StickerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.stream.Collectors;

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
        updateReviewImage(review, reviewUpdateDto.getUpdateImageFiles(), reviewUpdateDto.getDeleteImageIdList());


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
     * 가게별 리뷰 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public ReviewListResDto<ScrollResultDto<ReviewResDto, Object[]>> getReviewListOfStore(PageRequestDto pageRequestDto, Long storeId) {

        Pageable pageable = pageRequestDto.getPageable(Sort.by("reviewId").descending());

        Page<Object[]> results = reviewRepository.getReviewListOfStore(storeId, pageRequestDto.getIsOnlyImage(), pageable);

        Function<Object[], ReviewResDto> fn = (arr -> {

            Review review = (Review) arr[0];
            if(review != null){
                //작성자 프로필 이미지
                MemberImage memberImage = (MemberImage) arr[1];

                String profileImageUrl = null;
                if (memberImage != null){
                    profileImageUrl = memberImage.getImageUrl();
                }

                //리뷰 이미지
                List<ImageDto> reviewImageDtoList = new ArrayList<>();

                if (review.getReviewImageList() != null && !review.getReviewImageList().isEmpty()){
                    for (ReviewImage reviewImage : review.getReviewImageList()){
                        reviewImageDtoList.add(new ImageDto(reviewImage.getImageId(), reviewImage.getImageUrl()));
                    }
                }

                return new ReviewResDto(review, profileImageUrl, (long)arr[2], reviewImageDtoList);
            }
            return null;
        });

        return new ReviewListResDto<>(results.getTotalElements(), new ScrollResultDto<>(results, fn));
    }

    /**
     * 회원별 리뷰 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public ReviewListResDto<List<MyReviewResDto>> getMyReviewList(Long principalId) {

        List<Object[]> results = reviewRepository.getMyReviewList(principalId);

        List<MyReviewResDto> myReviewResDtoList = results.stream().map(arr -> {
            Review review = (Review) arr[0];

            //review 이미지 리스트
            List<ImageDto> reviewImageDtoList = new ArrayList<>();

            if (review.getReviewImageList() != null && !review.getReviewImageList().isEmpty()) {
                for (ReviewImage reviewImage : review.getReviewImageList()) {
                    reviewImageDtoList.add(new ImageDto(reviewImage.getImageId(), reviewImage.getImageUrl()));
                }
            }

            //store 이미지
            ImageDto storeImageDto = null;

            if (review.getStore().getStoreImageList() != null && !review.getStore().getStoreImageList().isEmpty()) {
                StoreImage storeImage = review.getStore().getStoreImageList().get(0);
                storeImageDto = new ImageDto(storeImage.getImageId(), storeImage.getImageUrl());
            }

            return new MyReviewResDto(review, (long) arr[1], reviewImageDtoList, storeImageDto);
        }).collect(Collectors.toList());

        return new ReviewListResDto<>(results.size(), myReviewResDtoList);
    }

    /**
     * 가게별 리뷰 리스트 개수 지정 조회
     */
    @Transactional(readOnly = true)
    @Override
    public ReviewListResDto<List<ReviewResDto>> getCustomLimitReviewListOfStore(int limit, Long storeId) {

        List<Object[]> results = reviewRepository.getCustomLimitReviewList(limit, storeId);

        List<ReviewResDto> reviewResDtoList = results.stream().map(arr -> {

            //작성자 프로필 이미지
            MemberImage memberImage = (MemberImage) arr[1];

            String profileImageUrl = null;
            if (memberImage != null) {
                profileImageUrl = memberImage.getImageUrl();
            }

            //리뷰 이미지
            Review review = (Review) arr[0];
            List<ImageDto> reviewImageDtoList = new ArrayList<>();
            if (review.getReviewImageList() != null && !review.getReviewImageList().isEmpty()) {

                for (ReviewImage reviewImage : review.getReviewImageList()) {
                    reviewImageDtoList.add(new ImageDto(reviewImage.getImageId(), reviewImage.getImageUrl()));
                }
            }

            return new ReviewResDto(review, profileImageUrl, (long) arr[2], reviewImageDtoList);
        }).collect(Collectors.toList());

        //카페 전체 리뷰 수 조회
        long totalCnt = reviewRepository.countByStoreStoreId(storeId);

        return new ReviewListResDto<>(totalCnt, reviewResDtoList);
    }

    /**
     * 카페별 상세 리뷰 점수 조회
     */
    @Transactional(readOnly = true)
    @Override
    public DetailReviewScoreResDto getDetailReviewScore(Long storeId) {

        //TODO 리뷰 데이터가 많이 쌓일 시, 캐시 사용 예정

        //카페 리뷰 데이터 조회
        List<Review> reviewList = reviewRepository.findByStoreId(storeId);

        //추천율, 항목별 가장 많이 받은 점수, 점수의 개수 찾기
        double recommendCnt = 0;
        int[] socketArr = new int[5];
        int[] wifiArr = new int[5];
        int[] restroomArr = new int[5];
        int[] tableSizeArr = new int[5];

        if(reviewList != null && !reviewList.isEmpty()){
            for(Review r : reviewList){

                //추천 count
                if(r.getRecommendation().equals(Recommendation.GOOD)){
                    recommendCnt += 1;
                }

                DetailEvaluation detailEvaluation = r.getDetailEvaluation();

                //socket 항목 count
                int socket = detailEvaluation.getSocket(); socketArr[socket] += 1;

                //wifi 항목 count
                int wifi = detailEvaluation.getWifi(); wifiArr[wifi] += 1;

                //restroom 항목 count
                int restroom = detailEvaluation.getRestroom(); restroomArr[restroom] += 1;

                //tableSize 항목 count
                int tableSize = detailEvaluation.getTableSize(); tableSizeArr[tableSize] += 1;
            }
        }
        //추천율 계산
        Double recommendPercent = null;
        if(reviewList != null && !reviewList.isEmpty()){
            recommendPercent = (recommendCnt / reviewList.size()) * 100;
        }

        //항목별 가장 많이 받은 점수, 점수의 개수 찾기
        String socketScoreOfMaxCnt = null; int maxCntOfSocket= -1;
        String wifiScoreOfMaxCnt = null; int maxCntOfWifi= -1;
        String restroomScoreOfMaxCnt = null; int maxCntOfRestroom= -1;
        String tableSizeScoreOfMaxCnt = null; int maxCntOfTableSize= -1;

        for(int i=1; i<5; i++){
            if(maxCntOfSocket <= socketArr[i]){
                maxCntOfSocket = socketArr[i]; socketScoreOfMaxCnt = String.valueOf(i);
            }
            if(maxCntOfWifi <= wifiArr[i]){
                maxCntOfWifi = wifiArr[i]; wifiScoreOfMaxCnt = String.valueOf(i);
            }
            if(maxCntOfRestroom <= restroomArr[i]){
                maxCntOfRestroom = restroomArr[i]; restroomScoreOfMaxCnt = String.valueOf(i);
            }
            if(maxCntOfTableSize <= tableSizeArr[i]){
                maxCntOfTableSize = tableSizeArr[i]; tableSizeScoreOfMaxCnt = String.valueOf(i);
            }
        }

        return new DetailReviewScoreResDto(reviewList.size(),recommendPercent,socketScoreOfMaxCnt, maxCntOfSocket,
                wifiScoreOfMaxCnt, maxCntOfWifi, restroomScoreOfMaxCnt, maxCntOfRestroom, tableSizeScoreOfMaxCnt, maxCntOfTableSize);
    }

    /**
     * 관리자단 리뷰 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminReviewListResDto getReviewListOfAdmin(PageRequestDto pageRequestDto) {

        Pageable pageable;

        if(pageRequestDto.getSort().equals("ASC")){
            pageable = pageRequestDto.getPageable(Sort.by("reviewId").ascending());
        }else{
            pageable = pageRequestDto.getPageable(Sort.by("reviewId").descending());
        }

        Page<Object[]> results = reviewRepository.getAllReviewList(pageRequestDto.getSearchType(), pageRequestDto.getKeyword(), pageable);

        Function<Object[], AdminReviewResDto> fn = (arr -> {

            Review review = (Review) arr[0];

            ImageDto imageDto = null;
            //리뷰 이미지
            if (review.getReviewImageList() != null && !review.getReviewImageList().isEmpty()) {

                ReviewImage reviewImage = review.getReviewImageList().get(0);

                imageDto = new ImageDto(reviewImage.getImageId(), reviewImage.getImageUrl());
            }

            return new AdminReviewResDto(review, imageDto, (Long) arr[1]);
        });

        return new AdminReviewListResDto(results.getTotalElements(), new PageResultDTO<>(results, fn));
    }

    /**
     * 관리자단 상세 리뷰 정보 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminDetailReviewResDto getDetailReviewOfAdmin(Long reviewId) {

        Object[] arr = reviewRepository.getDetailReview(reviewId).orElseThrow(() ->
                new CustomException("존재하지 않은 리뷰입니다."));

        Review review = (Review) arr[0];

        //review 이미지 리스트
        List<ImageDto> reviewImageDtoList = new ArrayList<>();
        if(review.getReviewImageList() != null && !review.getReviewImageList().isEmpty()){

            for(ReviewImage reviewImage : review.getReviewImageList()){
                reviewImageDtoList.add(new ImageDto(reviewImage.getImageId(), reviewImage.getImageUrl()));
            }
        }

        return new AdminDetailReviewResDto(review, (long)arr[1], reviewImageDtoList);
    }

    /**
     * 관리자단 카페 리뷰 상세 평가 정보 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminReviewEvaluationOfStoreResDto getReviewDetailEvaluationOfStore(Long storeId) {

        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new CustomException("존재하지 않는 카페입니다."));

        //추천율 계산
        Double recommendPercent = store.getRecommendPercent();

        List<Review> reviewList = store.getReviewList();

        //각 항목의 점수별 개수 count
        int[] socketArr = new int[5];
        int[] wifiArr = new int[5];
        int[] restroomArr = new int[5];
        int[] tableSizeArr = new int[5];

        if(reviewList != null && !reviewList.isEmpty()){
            for(Review r : reviewList){

                DetailEvaluation detailEvaluation = r.getDetailEvaluation();

                //socket 항목 count
                int socket = detailEvaluation.getSocket(); socketArr[socket] += 1;

                //wifi 항목 count
                int wifi = detailEvaluation.getWifi(); wifiArr[wifi] += 1;

                //restroom 항목 count
                int restroom = detailEvaluation.getRestroom(); restroomArr[restroom] += 1;

                //tableSize 항목 count
                int tableSize = detailEvaluation.getTableSize(); tableSizeArr[tableSize] += 1;
            }
        }

        AdminReviewScoreResDto socketScoreResDto = new AdminReviewScoreResDto(socketArr[1], socketArr[2], socketArr[3], socketArr[4]);
        AdminReviewScoreResDto wifiScoreResDto = new AdminReviewScoreResDto(wifiArr[1], wifiArr[2], wifiArr[3], wifiArr[4]);
        AdminReviewScoreResDto restroomScoreResDto = new AdminReviewScoreResDto(restroomArr[1], restroomArr[2], restroomArr[3], restroomArr[4]);
        AdminReviewScoreResDto tableSizeScoreResDto = new AdminReviewScoreResDto(tableSizeArr[1], tableSizeArr[2], tableSizeArr[3], tableSizeArr[4]);

        return new AdminReviewEvaluationOfStoreResDto(recommendPercent, socketScoreResDto,
                wifiScoreResDto, restroomScoreResDto, tableSizeScoreResDto);
    }
}
