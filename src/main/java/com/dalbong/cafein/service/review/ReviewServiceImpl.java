package com.dalbong.cafein.service.review;

import com.dalbong.cafein.domain.image.MemberImage;
import com.dalbong.cafein.domain.image.ReviewImage;
import com.dalbong.cafein.domain.image.StoreImage;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.domain.member.MemberState;
import com.dalbong.cafein.domain.memo.MemoRepository;
import com.dalbong.cafein.domain.memo.ReviewMemo;
import com.dalbong.cafein.domain.memo.ReviewMemoRepository;
import com.dalbong.cafein.domain.report.Report;
import com.dalbong.cafein.domain.report.ReportRepository;
import com.dalbong.cafein.domain.review.DetailEvaluation;
import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.review.ReviewRepository;
import com.dalbong.cafein.domain.sticker.ReviewSticker;
import com.dalbong.cafein.domain.sticker.ReviewStickerRepository;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.PossibleRegistrationResDto;
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
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final StoreRepository storeRepository;
    private final ImageService imageService;
    private final MemberRepository memberRepository;
    private final ReviewMemoRepository reviewMemoRepository;
    private final ReviewStickerRepository reviewStickerRepository;
    private final ReportRepository reportRepository;

    /**
     * 리뷰 등록 가능 여부 체크
     */
    @Transactional(readOnly = true)
    @Override
    public PossibleRegistrationResDto checkPossibleRegistration(Long storeId, Long principalId) {

        //회원 정지 상태 확인
        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));

        if(member.getState().equals(MemberState.SUSPENSION)){
            return new PossibleRegistrationResDto(false, "활동이 정지된 회원입니다.");
        }

        //금일 해당 카페에 리뷰 작성 여부 확인
        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new CustomException("존재하지 않는 카페입니다."));

        if(reviewRepository.existRegisterToday(store.getStoreId(), principalId)){
            return new PossibleRegistrationResDto(false, "하루당 한 카페에 리뷰 등록은 한번만 가능합니다.");
        }

        return new PossibleRegistrationResDto(true, null);
    }

    /**
     * 리뷰 등록
     */
    @Transactional
    @Override
    public Review register(ReviewRegDto reviewRegDto, Long principalId) throws IOException {

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));

        //회원 정지 상태 확인
        if(member.getState().equals(MemberState.SUSPENSION)){
            throw new CustomException("활동이 정지된 회원입니다.");
        }

        Store store = storeRepository.findById(reviewRegDto.getStoreId()).orElseThrow(() ->
                new CustomException("존재하지 않는 카페입니다."));


        //금일 해당 카페에 리뷰 작성 여부 확인
        boolean existResult = reviewRepository.existRegisterToday(store.getStoreId(), principalId);

        if(existResult){
            throw new CustomException("하루당 한 카페에 리뷰 등록은 한번만 가능합니다.");
        }


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

    /**
     * 리뷰 게시 상태로 변경
     */
    @Transactional
    @Override
    public void post(Long reviewId) {

        Review review = reviewRepository.findById(reviewId).orElseThrow(() ->
                new CustomException("존재하지 않는 리뷰입니다."));

        review.post();
    }

    /**
     * 리뷰 게시 중단 상태로 변경
     */
    @Transactional
    @Override
    public void stopPosting(Long reviewId) {

        Review review = reviewRepository.findById(reviewId).orElseThrow(() ->
                new CustomException("존재하지 않는 리뷰입니다."));

        review.stopPosting();
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

        //메모 삭제
        reviewMemoRepository.deleteByReview(review);

        //스티커 - reviewId null
        Optional<ReviewSticker> result = reviewStickerRepository.findByReview(review);
        if(result.isPresent()){
            ReviewSticker reviewSticker = result.get();
            reviewSticker.changeNullReview();
        }

        //신고 - reviewId null
        List<Report> findReportList = reportRepository.findByReview(review);
        if(findReportList != null){
            for(Report report : findReportList){
                report.changeNullReview();
            }
        }

        //리뷰 이미지 삭제
        List<ReviewImage> reviewImageList = review.getReviewImageList();

        for (ReviewImage reviewImage : reviewImageList){
            imageService.remove(reviewImage.getImageId());
        }

        //리뷰 삭제
        reviewRepository.deleteById(reviewId);
    }

    /**
     * 카페별 리뷰 리스트 조회
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
                List<ImageDto> reviewImageDtoList = getReviewImageDtoList(review);

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

            if(review != null){

                //review 이미지 리스트
                List<ImageDto> reviewImageDtoList = getReviewImageDtoList(review);

                //TODO 대표 이미지로 변경
                //store 이미지
                ImageDto storeImageDto = null;

                if (review.getStore().getStoreImageList() != null && !review.getStore().getStoreImageList().isEmpty()) {
                    StoreImage storeImage = review.getStore().getStoreImageList().get(0);
                    storeImageDto = new ImageDto(storeImage.getImageId(), storeImage.getImageUrl());
                }

                return new MyReviewResDto(review, (long) arr[1], reviewImageDtoList, storeImageDto);
                }
            return null;
            }).collect(Collectors.toList());


        return new ReviewListResDto<>(results.size(), myReviewResDtoList);
    }

    /**
     * 카페별 리뷰 리스트 개수 지정 조회
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

            List<ImageDto> reviewImageDtoList = getReviewImageDtoList(review);

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
        int[] socketArr = new int[6];
        int[] wifiArr = new int[6];
        int[] restroomArr = new int[6];
        int[] tableSizeArr = new int[6];

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

        for(int i=1; i<6; i++){
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
     * 관리자단 리뷰 수정
     */
    @Transactional
    @Override
    public void modifyOfAdmin(AdminReviewUpdateDto adminReviewUpdateDto) throws IOException {

        Review review = reviewRepository.findById(adminReviewUpdateDto.getReviewId()).orElseThrow(() ->
                new CustomException("존재하는 리뷰가 없습니다."));

        //리뷰 이미지 갱신
        updateReviewImage(review, adminReviewUpdateDto.getUpdateImageFiles(), adminReviewUpdateDto.getDeleteImageIdList());
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

            return new AdminReviewResDto(review, (Long) arr[1]);
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
        List<ImageDto> reviewImageDtoList = getReviewImageDtoList(review);

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
        int[] socketArr = new int[6];
        int[] wifiArr = new int[6];
        int[] restroomArr = new int[6];
        int[] tableSizeArr = new int[6];

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
        AdminReviewScoreResDto socketScoreResDto = new AdminReviewScoreResDto(socketArr[1], socketArr[2], socketArr[3], socketArr[4], socketArr[5]);
        AdminReviewScoreResDto wifiScoreResDto = new AdminReviewScoreResDto(wifiArr[1], wifiArr[2], wifiArr[3], wifiArr[4], wifiArr[5]);
        AdminReviewScoreResDto restroomScoreResDto = new AdminReviewScoreResDto(restroomArr[1], restroomArr[2], restroomArr[3], restroomArr[4], restroomArr[5]);
        AdminReviewScoreResDto tableSizeScoreResDto = new AdminReviewScoreResDto(tableSizeArr[1], tableSizeArr[2], tableSizeArr[3], tableSizeArr[4], tableSizeArr[5]);

        return new AdminReviewEvaluationOfStoreResDto(recommendPercent, socketScoreResDto,
                wifiScoreResDto, restroomScoreResDto, tableSizeScoreResDto);
    }

    /**
     * 관리자단 회원별 리뷰 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public List<AdminReviewResDto> getReviewListByMemberIdOfAdmin(Long memberId) {

        List<Object[]> results = reviewRepository.getReviewListOfMember(memberId);

        return results.stream().map(arr -> {
            Review review = (Review) arr[0];
            Long memoId = (Long) arr[1];

            return new AdminReviewResDto(review, memoId);
        }).collect(Collectors.toList());
    }

    /**
     * 오늘 등록된 리뷰 수 조회
     */
    @Transactional(readOnly = true)
    @Override
    public Long getRegisterCountOfToday() {
        return (Long) reviewRepository.getRegisterCountOfToday();
    }

    /**
     * 본인 등록한 리뷰 수 조회
     */
    @Transactional(readOnly = true)
    @Override
    public int countMyReview(Long memberId) {

        return reviewRepository.countByMemberId(memberId);
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
