package com.dalbong.cafein.service.store;

import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.businessHours.BusinessHoursRepository;
import com.dalbong.cafein.domain.congestion.Congestion;
import com.dalbong.cafein.domain.congestion.CongestionRepository;
import com.dalbong.cafein.domain.image.*;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.domain.member.MemberState;
import com.dalbong.cafein.domain.memo.StoreMemoRepository;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.sticker.StoreSticker;
import com.dalbong.cafein.domain.sticker.StoreStickerRepository;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.domain.store.dto.StoreQueryDto;
import com.dalbong.cafein.dto.admin.store.AdminDetailStoreResDto;
import com.dalbong.cafein.dto.admin.store.AdminMyStoreResDto;
import com.dalbong.cafein.dto.admin.store.AdminStoreListDto;
import com.dalbong.cafein.dto.admin.store.AdminStoreResDto;
import com.dalbong.cafein.dto.businessHours.BusinessHoursInfoDto;
import com.dalbong.cafein.dto.businessHours.BusinessHoursUpdateDto;
import com.dalbong.cafein.dto.image.ImageDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.page.PageResultDTO;
import com.dalbong.cafein.dto.store.*;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.service.congestion.CongestionService;
import com.dalbong.cafein.service.image.ImageService;
import com.dalbong.cafein.service.nearStoreToSubwayStation.NearStoreToSubWayStationService;
import com.dalbong.cafein.service.review.ReviewService;
import com.dalbong.cafein.util.DistanceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService{

    private final StoreRepository storeRepository;
    private final BusinessHoursRepository businessHoursRepository;
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final ReviewService reviewService;
    private final ReviewImageRepository reviewImageRepository;
    private final StoreImageRepository storeImageRepository;
    private final MemberRepository memberRepository;
    private final StoreMemoRepository storeMemoRepository;
    private final StoreStickerRepository storeStickerRepository;
    private final CongestionService congestionService;
    private final CongestionRepository congestionRepository;
    private final NearStoreToSubWayStationService nearStoreToSubWayStationService;


    /**
     * 카페 등록
     */
    @Transactional
    @Override
    public Store register(StoreRegDto storeRegDto, Long principalId) throws IOException {

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));

        //회원 정지 상태 확인
        if(member.getState().equals(MemberState.SUSPENSION)){
            throw new CustomException("활동이 정지된 회원입니다.");
        }

        //카페 중복 등록 예외 처리
        if(storeRepository.existAddress(storeRegDto.getAddress())){
            throw new CustomException("이미 등록된 카페입니다.");
        }

        //businessHours 엔티티 저장
        BusinessHours businessHours = storeRegDto.toBusinessHoursEntity();
        businessHoursRepository.save(businessHours);

        //store entity 저장
        Store store = storeRegDto.toEntity(principalId);
        store.changeBusinessHours(businessHours);

        storeRepository.save(store);

        imageService.saveStoreImage(store, member, storeRegDto.getImageFiles(), false);

        //리뷰 자동 등록
        reviewService.register(storeRegDto.toReviewRegDto(store.getStoreId()), principalId);

        //가까운 역 데이터 저장
        nearStoreToSubWayStationService.save(store);

        return store;
    }

    /**
     * 카페 수정
     */
    @Transactional
    @Override
    public void modify(StoreUpdateDto storeUpdateDto, Long principalId) throws IOException {

        Store store = storeRepository.findById(storeUpdateDto.getStoreId()).orElseThrow(() ->
                new CustomException("존재하지 않는 카페입니다."));

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));

        //카페 데이터 수정
        store.changeWebsite(storeUpdateDto.getWebsite());
        store.changePhone(storeUpdateDto.getPhone());
        store.changeWifiPassword(storeUpdateDto.getWifiPassword());

        //영업시간 수정
        BusinessHours FindBusinessHours = store.getBusinessHours();
        BusinessHoursUpdateDto businessHoursUpdateDto = storeUpdateDto.toBusinessHoursUpdateDto();

        //기존 영업시간 데이터가 있는 경우
        if(FindBusinessHours != null){
            FindBusinessHours.changeOnMon(businessHoursUpdateDto.getOnMon());
            FindBusinessHours.changeOnTue(businessHoursUpdateDto.getOnTue());
            FindBusinessHours.changeOnWed(businessHoursUpdateDto.getOnWed());
            FindBusinessHours.changeOnThu(businessHoursUpdateDto.getOnThu());
            FindBusinessHours.changeOnFri(businessHoursUpdateDto.getOnFri());
            FindBusinessHours.changeOnSat(businessHoursUpdateDto.getOnSat());
            FindBusinessHours.changeOnSun(businessHoursUpdateDto.getOnSun());
            FindBusinessHours.changeEtcTime(businessHoursUpdateDto.getEtcTime());
        }
        //기존 영업시간 데이터가 없는 경우
        else{
            BusinessHours businessHours = storeUpdateDto.toBusinessHoursEntity();
            businessHoursRepository.save(businessHours);
            store.changeBusinessHours(businessHours);
        }

        //이미지 추가
        updateStoreImage(store, member, storeUpdateDto.getUpdateImageFiles(), storeUpdateDto.getDeleteImageIdList(), false);

        //최신 수정자 변경
        store.changeModMember(member);
    }

    /**
     * 카페 대표 이미지 설정
     */
    @Transactional
    @Override
    public void setUpRepresentImage(Long storeId, Long presentImageId) {

        //기존 대표 이미지 취소
        Image oldRepresentImage = imageService.getRepresentImageOfStore(storeId);

        if(oldRepresentImage != null){
            oldRepresentImage.cancelRepresentative();
        }

        //새로운 대표 이미지 설정
        Image newRepresentImage = imageRepository.findById(presentImageId).orElseThrow(() ->
                new CustomException("존재하지 않는 이미지입니다."));

        newRepresentImage.setUpRepresentative();
    }

    private void updateStoreImage(Store store, Member regMember, List<MultipartFile> updateImageFiles, List<Long> deleteImageIdList, boolean isCafein) throws IOException {

        //이미지 추가
        imageService.saveStoreImage(store, regMember, updateImageFiles, isCafein);

        //이미지 삭제
        if (deleteImageIdList != null && !deleteImageIdList.isEmpty()){
            for (Long imageId : deleteImageIdList){
                imageService.remove(imageId);
            }
        }
    }

    /**
     * 카페 삭제
     */
    @Transactional
    @Override
    public void remove(Long storeId) {

        Store store = storeRepository.findById(storeId).orElseThrow(() ->
                new CustomException("존재하지 않는 카페입니다."));

        //memo 삭제
        storeMemoRepository.deleteByStore(store);

        //스티커 storeId null
        Optional<StoreSticker> result = storeStickerRepository.findByStore(store);
        if(result.isPresent()){
            StoreSticker storeSticker = result.get();
            storeSticker.changeNullStore();
        }

        //혼잡도 삭제
        List<Congestion> congestionList = congestionRepository.findByStore(store);

        for(Congestion congestion : congestionList){
            congestionService.remove(congestion.getCongestionId());
        }

        //리뷰 삭제
        List<Review> reviewList = store.getReviewList();

        for(Review review : reviewList){
            reviewService.remove(review.getReviewId());
        }

        //store 이미지 삭제
        List<StoreImage> storeImageList = store.getStoreImageList();

        for(StoreImage storeImage : storeImageList){
            imageService.remove(storeImage.getImageId());
        }

        //store 삭제 (Cascade.Remove - Heart, businessHours, nearStoreToSubwayStation)
        storeRepository.deleteById(storeId);
    }

    /**
     * 앱단 카페 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public List<StoreResDto> getStoreList(StoreSearchRequestDto storeSearchRequestDto, Long principal) {

        List<StoreQueryDto> results = storeRepository.getStoreList(
                storeSearchRequestDto.getKeyword(),
                storeSearchRequestDto.getCenterCoordinates(),
                storeSearchRequestDto.getUserCoordinates(),
                storeSearchRequestDto.getRect());

        return results.stream().map(storeQueryDto -> {

            Store store = storeQueryDto.getStore();

            //리뷰 추천율
            Double recommendPercent = store.getRecommendPercent();

            //카페 영업시간 데이터
            Map<String, Object> businessInfoMap = store.getBusinessInfo();
            BusinessHoursInfoDto businessHoursInfoDto = new BusinessHoursInfoDto(businessInfoMap);

            //최대 이미지 4개 불러오기
            List<ImageDto> imageDtoList = imageService.getCustomSizeImageList(store, 4);

            return new StoreResDto(store, recommendPercent, businessHoursInfoDto, storeQueryDto.getUserDistance(),
                    imageDtoList, storeQueryDto.getHeartCnt(), storeQueryDto.getCongestionAvg(), principal);
        }).collect(Collectors.toList());
    }

    /**
     * 앱단 내 카페 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public StoreListResDto<List<MyStoreResDto>> getMyStoreList(Long principalId) {

        List<Object[]> results = storeRepository.getMyStoreList(principalId);

        List<MyStoreResDto> myStoredResDtoList = results.stream().map(arr -> {

            Store store = (Store) arr[0];

            //카페 영업시간 데이터
            Map<String, Object> businessInfoMap = store.getBusinessInfo();
            BusinessHoursInfoDto businessHoursInfoDto = new BusinessHoursInfoDto(businessInfoMap);

            //첫번째 이미지 불러오기
            ImageDto imageDto = null;

            List<ImageDto> imageDtoList = imageService.getCustomSizeImageList(store, 1);

            if(!imageDtoList.isEmpty()){
                imageDto = imageDtoList.get(0);
            }

            return new MyStoreResDto(store, businessHoursInfoDto, imageDto, (Double) arr[1]);
        }).collect(Collectors.toList());

        return new StoreListResDto<>(myStoredResDtoList.size(), myStoredResDtoList);
    }

    /**
     * 앱단 내 가게 리스트 개수지정 조회
     */
    @Transactional(readOnly = true)
    @Override
    public StoreListResDto<List<MyStoreResDto>> getCustomLimitMyStoreList(int limit, Long principalId) {

        List<Object[]> results = storeRepository.getCustomLimitMyStoreList(limit, principalId);

        List<MyStoreResDto> myStoredResDtoList = results.stream().map(arr -> {

            Store store = (Store) arr[0];

            //카페 영업시간 데이터
            Map<String, Object> businessInfoMap = store.getBusinessInfo();
            BusinessHoursInfoDto businessHoursInfoDto = new BusinessHoursInfoDto(businessInfoMap);

            //첫번째 이미지 불러오기
            ImageDto imageDto = null;

            List<ImageDto> imageDtoList = imageService.getCustomSizeImageList(store, 1);

            if(!imageDtoList.isEmpty()){
                imageDto = imageDtoList.get(0);
            }

            return new MyStoreResDto(store, businessHoursInfoDto, imageDto, (Double) arr[1]);
        }).collect(Collectors.toList());

        return new StoreListResDto<>(myStoredResDtoList.size(), myStoredResDtoList);
    }

    /**
     * 앱단 본인이 등록한 가게 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public StoreListResDto<List<MyRegisterStoreResDto>> getMyRegisterStoreList(Long principalId) {

        List<Object[]> results = storeRepository.getMyRegisterStoreList(principalId);

        List<MyRegisterStoreResDto> myRegisterStoreResDtoList = results.stream().map(arr -> {

            Store store = (Store) arr[0];

            //카페 영업시간 데이터
            Map<String, Object> businessInfoMap = store.getBusinessInfo();
            BusinessHoursInfoDto businessHoursInfoDto = new BusinessHoursInfoDto(businessInfoMap);

            //첫번째 이미지 불러오기
            ImageDto imageDto = null;

            List<ImageDto> imageDtoList = imageService.getCustomSizeImageList(store, 1);

            if(!imageDtoList.isEmpty()){
                imageDto = imageDtoList.get(0);
            }

            return new MyRegisterStoreResDto(store, businessHoursInfoDto, (Double) arr[1], imageDto);
        }).collect(Collectors.toList());

        return new StoreListResDto<>(results.size(), myRegisterStoreResDtoList);
    }

    /**
     * 근처 카공 카페 리스트 조회 - 조회중인 카페 기준
     */
    @Transactional(readOnly = true)
    @Override
    public List<NearStoreResDto> getNearStoreListOfStore(Long storeId, Long principalId) {

        Store findStore = storeRepository.findById(storeId).orElseThrow(() ->
                new CustomException("존재하지 않는 카페입니다."));

        List<Object[]> results = storeRepository.getNearStoreListOfStore(storeId, findStore.getLatY(), findStore.getLngX());

        return results.stream().map(arr -> {

            Store store = (Store) arr[0];

            //리뷰 추천율
            Double recommendPercent = store.getRecommendPercent();

            //카페 영업시간 데이터
            Map<String, Object> businessInfoMap = store.getBusinessInfo();
            BusinessHoursInfoDto businessHoursInfoDto = new BusinessHoursInfoDto(businessInfoMap);

            //최대 이미지 3개 불러오기
            List<ImageDto> imageDtoList = imageService.getCustomSizeImageList(store, 3);

            return new NearStoreResDto(store, recommendPercent, businessHoursInfoDto, imageDtoList, (double) arr[1], (Double) arr[2], principalId);
        }).collect(Collectors.toList());

    }

    /**
     * 앱단 카페 상세 페이지 조회
     */
    @Transactional
    @Override
    public DetailStoreResDto getDetailStore(Long storeId, Long principalId) {

        Object[] arr = storeRepository.getDetailStore(storeId).orElseThrow(() ->
                new CustomException("존재하지 않은 카페입니다."));

        Store store = (Store) arr[0];

        //member 이미지
        MemberImage memberImage = (MemberImage) arr[1];
        ImageDto memberImageDto = null;
        if (memberImage != null){
            memberImageDto = new ImageDto(memberImage.getImageId(), memberImage.getImageUrl());
        }

        //대표 이미지
        Image representImage = imageService.getRepresentImageOfStore(storeId);

        ImageDto representImageDto = toRepresentImageDto(representImage);

        //review 이미지 리스트
        List<ImageDto> reviewImageDtoList = getReviewImageDtoList(store, representImage);

        //store 이미지 리스트
        List<ImageDto> storeImageDtoList = getStoreImageDtoList(store, representImage);

        //조회수 증가
        store.increaseViewCnt();

        return new DetailStoreResDto(store, (Double) arr[2], memberImageDto,
                representImageDto, reviewImageDtoList, storeImageDtoList, principalId);
    }

    /**
     * 관리자단 카페 등록
     */
    @Transactional
    @Override
    public Store registerOfAdmin(StoreRegDto storeRegDto, Long principalId) throws IOException {

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));

        //카페 중복 등록 예외 처리
        if(storeRepository.existAddress(storeRegDto.getAddress())){
            throw new CustomException("이미 등록된 카페입니다.");
        }

        //businessHours 엔티티 저장
        BusinessHours businessHours = storeRegDto.toBusinessHoursEntity();
        businessHoursRepository.save(businessHours);

        //store entity 저장
        Store store = storeRegDto.toEntity(principalId);
        store.changeBusinessHours(businessHours);

        storeRepository.save(store);

        imageService.saveStoreImage(store, member, storeRegDto.getImageFiles(), true);

        //리뷰 자동 등록
        reviewService.register(storeRegDto.toReviewRegDto(store.getStoreId()), principalId);

        //가까운 역 데이터 저장
        nearStoreToSubWayStationService.save(store);

        return store;
    }

    /**
     * 관리자단 카페 수정
     */
    @Transactional
    @Override
    public void modifyOfAdmin(StoreUpdateDto storeUpdateDto, Long principalId) throws IOException {

        Store store = storeRepository.findById(storeUpdateDto.getStoreId()).orElseThrow(() ->
                new CustomException("존재하지 않는 카페입니다."));

        Member member = memberRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하지 않는 회원입니다."));

        //카페 데이터 수정
        store.changeWebsite(storeUpdateDto.getWebsite());
        store.changePhone(storeUpdateDto.getPhone());
        store.changeWifiPassword(storeUpdateDto.getWifiPassword());

        //영업시간 수정
        BusinessHours FindBusinessHours = store.getBusinessHours();
        BusinessHoursUpdateDto businessHoursUpdateDto = storeUpdateDto.toBusinessHoursUpdateDto();

        //기존 영업시간 데이터가 있는 경우
        if(FindBusinessHours != null){
            FindBusinessHours.changeOnMon(businessHoursUpdateDto.getOnMon());
            FindBusinessHours.changeOnTue(businessHoursUpdateDto.getOnTue());
            FindBusinessHours.changeOnWed(businessHoursUpdateDto.getOnWed());
            FindBusinessHours.changeOnThu(businessHoursUpdateDto.getOnThu());
            FindBusinessHours.changeOnFri(businessHoursUpdateDto.getOnFri());
            FindBusinessHours.changeOnSat(businessHoursUpdateDto.getOnSat());
            FindBusinessHours.changeOnSun(businessHoursUpdateDto.getOnSun());
            FindBusinessHours.changeEtcTime(businessHoursUpdateDto.getEtcTime());
        }
        //기존 영업시간 데이터가 없는 경우
        else{
            BusinessHours businessHours = storeUpdateDto.toBusinessHoursEntity();
            businessHoursRepository.save(businessHours);
            store.changeBusinessHours(businessHours);
        }

        //이미지 추가
        updateStoreImage(store, member, storeUpdateDto.getUpdateImageFiles(), storeUpdateDto.getDeleteImageIdList(), true);

        //최신 수정자 변경
        store.changeModMember(Member.builder().memberId(principalId).build());
    }

    /**
     * 관리자단 카페 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminStoreListDto getStoreListOfAdmin(PageRequestDto pageRequestDto) {

        Pageable pageable;

        if(pageRequestDto.getSort().equals("ASC")){
            pageable = pageRequestDto.getPageable(Sort.by("storeId").ascending());
        }else{
            pageable = pageRequestDto.getPageable(Sort.by("storeId").descending());
        }

        Page<Object[]> results = storeRepository.getAllStoreList(pageRequestDto.getSggNms(),
                pageRequestDto.getSearchType(), pageRequestDto.getKeyword(), pageable);

        Function<Object[], AdminStoreResDto> fn = (arr -> {

            Store store = (Store) arr[0];
            int reviewCnt = (int) arr[1];

            //리뷰 추천율
            Double recommendPercent = store.getRecommendPercent();

            //첫번째 이미지 불러오기
            ImageDto imageDto = null;

            List<ImageDto> imageDtoList = imageService.getCustomSizeImageList(store, 1);

            if(!imageDtoList.isEmpty()){
                imageDto = imageDtoList.get(0);
            }

            return new AdminStoreResDto(store, reviewCnt, recommendPercent, (Double) arr[2], imageDto, (Long) arr[3]);
        });

        return new AdminStoreListDto(results.getTotalElements(), new PageResultDTO<>(results, fn));
    }

    /**
     * 관리자단 카페 상세 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminDetailStoreResDto getDetailStoreOfAdmin(Long storeId) {

        Object[] arr = storeRepository.getDetailStoreOfAdmin(storeId).orElseThrow(() ->
                new CustomException("존재하지 않는 카페입니다."));

        Store store = (Store) arr[0];

        //대표 이미지
        Image representImage = imageService.getRepresentImageOfStore(storeId);
        ImageDto representImageDto = toRepresentImageDto(representImage);

        //review 이미지 리스트
        List<ImageDto> reviewImageDtoList = getReviewImageDtoList(store, representImage);

        //store 이미지 리스트
        List<ImageDto> storeImageDtoList = getStoreImageDtoList(store, representImage);

        return new AdminDetailStoreResDto(store, (int)arr[1], (long) arr[2], (int)arr[3],
                representImageDto, reviewImageDtoList, storeImageDtoList);
    }

    /**
     * 관리자단 회원별 내 카페 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public List<AdminMyStoreResDto> getMyStoreByMemberIdOfAdmin(Long memberId) {

        List<Store> storeList = storeRepository.getMyStoreByMemberIdOfAdmin(memberId);

        return storeList.stream().map(store -> new AdminMyStoreResDto(store)).collect(Collectors.toList());
    }

    /**
     * 오늘 등록된 카페 수 조회
     */
    @Transactional(readOnly = true)
    @Override
    public Long getRegisterCountOfToday() {
        return (Long) storeRepository.getRegisterCountOfToday();
    }

    /**
     * 본인이 등록한 카페 수 조회
     */
    @Transactional(readOnly = true)
    @Override
    public int countMyRegisterStore(Long memberId) {

        return storeRepository.countByRegMemberId(memberId);
    }

    private ImageDto toRepresentImageDto(Image representImage) {

        ImageDto representImageDto = null;

        //StoreImage 일 경우
        if(representImage instanceof StoreImage){
            StoreImage storeImage = (StoreImage) representImage;

            //지연 로딩 발생
            representImageDto = new ImageDto(storeImage.getImageId(), storeImage.getImageUrl(),
                    storeImage.getRegMember().getNickname(), storeImage.getIsCafein());
        }

        //reviewImage 일 경우
        if(representImage instanceof ReviewImage){
            ReviewImage reviewImage = (ReviewImage) representImage;

            //지연 로딩 발생
            representImageDto = new ImageDto(reviewImage.getImageId(), reviewImage.getImageUrl(),
                    reviewImage.getReview().getMember().getNickname());
        }

        return representImageDto;
    }

    private List<ImageDto> getReviewImageDtoList(Store store, Image representImage) {

        List<ImageDto> reviewImageDtoList = new ArrayList<>();
        List<ReviewImage> reviewImageList = reviewImageRepository.findWithRegMemberByStoreId(store.getStoreId());

        if(!reviewImageList.isEmpty()){
            //최신순 조회
            Collections.reverse(reviewImageList);

            for(ReviewImage reviewImage : reviewImageList){
                //대표 이미지는 통과
                if(reviewImage.equals(representImage)) continue;

                reviewImageDtoList.add(new ImageDto(reviewImage.getImageId(), reviewImage.getImageUrl(),
                        reviewImage.getReview().getMember().getNickname()));
            }
        }
        return reviewImageDtoList;
    }

    private List<ImageDto> getStoreImageDtoList(Store store, Image representImage) {

        List<ImageDto> storeImageDtoList = new ArrayList<>();

        List<StoreImage> storeImageList = storeImageRepository.findWithRegMemberByStoreId(store.getStoreId());

        if(storeImageList != null && !storeImageList.isEmpty()){

            //최신순 조회
            Collections.reverse(storeImageList);

            for (StoreImage storeImage : storeImageList){
                //대표 이미지는 통과
                if(storeImage.equals(representImage)) continue;

                storeImageDtoList.add(new ImageDto(storeImage.getImageId(), storeImage.getImageUrl(),
                        storeImage.getRegMember().getNickname(), storeImage.getIsCafein()));
            }
        }
        return storeImageDtoList;
    }
}

