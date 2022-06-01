package com.dalbong.cafein.service.store;

import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.businessHours.BusinessHoursRepository;
import com.dalbong.cafein.domain.image.MemberImage;
import com.dalbong.cafein.domain.image.ReviewImage;
import com.dalbong.cafein.domain.image.ReviewImageRepository;
import com.dalbong.cafein.domain.image.StoreImage;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.admin.store.AdminDetailStoreResDto;
import com.dalbong.cafein.dto.admin.store.AdminStoreListDto;
import com.dalbong.cafein.dto.admin.store.AdminStoreResDto;
import com.dalbong.cafein.dto.businessHours.BusinessHoursUpdateDto;
import com.dalbong.cafein.dto.image.ImageDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.page.PageResultDTO;
import com.dalbong.cafein.dto.store.*;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.service.image.ImageService;
import com.dalbong.cafein.service.review.ReviewService;
import com.dalbong.cafein.service.sticker.StickerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService{

    private final StoreRepository storeRepository;
    private final BusinessHoursRepository businessHoursRepository;
    private final ImageService imageService;
    private final ReviewService reviewService;
    private final ReviewImageRepository reviewImageRepository;

    /**
     * 카페 등록
     */
    @Transactional
    @Override
    public Store register(StoreRegDto storeRegDto, Long principalId) throws IOException {

        //businessHours 엔티티 저장
        BusinessHours businessHours = storeRegDto.toBusinessHoursEntity();
        businessHoursRepository.save(businessHours);

        //store entity 저장
        Store store = storeRegDto.toEntity(principalId);
        store.changeBusinessHours(businessHours);

        storeRepository.save(store);

        imageService.saveStoreImage(store, storeRegDto.getImageFiles());

        //리뷰 자동 등록
        reviewService.register(storeRegDto.toReviewRegDto(store.getStoreId()), principalId);

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

        //카페 데이터 수정
        store.changeStoreName(storeUpdateDto.getStoreName());
        store.changeAddress(storeUpdateDto.getAddress());
        store.changeWebsite(storeUpdateDto.getWebsite());
        store.changePhone(storeUpdateDto.getPhone());
        store.changeWifiPassword(storeUpdateDto.getWifiPassword());
        store.changeLatAndLng(storeUpdateDto.getLngX(), storeUpdateDto.getLatY());

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
        updateStoreImage(store, storeUpdateDto.getUpdateImageFiles(), storeUpdateDto.getDeleteImageIdList());

        //최신 수정자 변경
        store.changeModMember(Member.builder().memberId(principalId).build());
    }

    private void updateStoreImage(Store store, List<MultipartFile> updateImageFiles, List<Long> deleteImageIdList) throws IOException {

        //이미지 추가
        imageService.saveStoreImage(store, updateImageFiles);

        //이미지 삭제
        if (deleteImageIdList != null && !deleteImageIdList.isEmpty()){
            for (Long imageId : deleteImageIdList){
                imageService.remove(imageId);
            }
        }
    }

    /**
     * 앱단 카페 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public List<StoreResDto> getStoreList(String keyword) {

        List<Object[]> results = storeRepository.getStoreList(keyword);

        return results.stream().map(arr -> {

            Store store = (Store) arr[0];

            //리뷰 추천율
            Double recommendPercent = store.getRecommendPercent();

            //카페 영업중 체크
            Boolean isOpen = store.checkIsOpen();

            //첫번째 이미지 불러오기
            ImageDto imageDto = null;
            if (store.getStoreImageList() != null && !store.getStoreImageList().isEmpty()) {

                StoreImage storeImage = store.getStoreImageList().get(0);

                imageDto = new ImageDto(storeImage.getImageId(), storeImage.getImageUrl());
            }

            return new StoreResDto(store, recommendPercent, isOpen, imageDto, (int) arr[1], (Double) arr[2]);
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

            //카페 영업중 체크
            Boolean isOpen = store.checkIsOpen();

            //첫번째 이미지 불러오기
            ImageDto imageDto = null;
            if (store.getStoreImageList() != null && !store.getStoreImageList().isEmpty()) {

                StoreImage storeImage = store.getStoreImageList().get(0);

                imageDto = new ImageDto(storeImage.getImageId(), storeImage.getImageUrl());
            }

            return new MyStoreResDto(store, isOpen, imageDto, (Double) arr[1]);
        }).collect(Collectors.toList());

        return new StoreListResDto<>(myStoredResDtoList.size(), myStoredResDtoList);
    }

    /**
     * 앱단 본인이 등록한 가게 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public StoreListResDto<List<MyRegisterStoreResDto>> getMyRegisterStoreList(Long principalId) {

        List<Store> results = storeRepository.getMyRegisterStoreList(principalId);

        List<MyRegisterStoreResDto> myRegisterStoreResDtoList = results.stream().map(store -> {

            //첫번째 이미지 불러오기
            ImageDto imageDto = null;
            if (store.getStoreImageList() != null && !store.getStoreImageList().isEmpty()) {

                StoreImage storeImage = store.getStoreImageList().get(0);

                imageDto = new ImageDto(storeImage.getImageId(), storeImage.getImageUrl());
            }

            return new MyRegisterStoreResDto(store, imageDto, principalId);
        }).collect(Collectors.toList());

        return new StoreListResDto<>(results.size(), myRegisterStoreResDtoList);
    }

    /**
     * 앱단 본인이 등록한 가게 리스트 개수지정 조회
     */
    @Transactional(readOnly = true)
    @Override
    public StoreListResDto<List<MyRegisterStoreResDto>> getCustomLimitMyRegisterStoreList(int limit, Long principalId) {

        List<Store> results = storeRepository.getCustomLimitReviewList(limit, principalId);

        List<MyRegisterStoreResDto> myRegisterStoreResDtoList = results.stream().map(store -> {

            //첫번째 이미지 불러오기
            ImageDto imageDto = null;
            if (store.getStoreImageList() != null && !store.getStoreImageList().isEmpty()) {

                StoreImage storeImage = store.getStoreImageList().get(0);

                imageDto = new ImageDto(storeImage.getImageId(), storeImage.getImageUrl());
            }

            return new MyRegisterStoreResDto(store, imageDto, principalId);
        }).collect(Collectors.toList());

        return new StoreListResDto<>(results.size(), myRegisterStoreResDtoList);
    }

    /**
     * 추천 검색 카페 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public List<RecommendSearchStoreResDto> getRecommendSearchStoreList(String keyword) {

        List<Store> results = storeRepository.getRecommendSearchStoreList(keyword);

        return results.stream().map(s -> new RecommendSearchStoreResDto(s)).collect(Collectors.toList());

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
            Long imageId = memberImage.getImageId();
            String imageUrl = memberImage.getImageUrl();
            memberImageDto = new ImageDto(imageId, imageUrl);
        }


        //totalImageList (review 이미지 + store 이미지)
        List<ImageDto> totalImageDtoList = new ArrayList<>();

        //review 이미지 리스트
        List<ReviewImage> reviewImageList = reviewImageRepository.findByStoreId(storeId);
        if(!reviewImageList.isEmpty()){
            for(ReviewImage reviewImage : reviewImageList){
                totalImageDtoList.add(new ImageDto(reviewImage.getImageId(), reviewImage.getImageUrl()));
            }
        }

        //store 이미지 리스트
        if(store.getStoreImageList() != null && !store.getStoreImageList().isEmpty()){
            for (StoreImage storeImage : store.getStoreImageList()){
                totalImageDtoList.add(new ImageDto(storeImage.getImageId(), storeImage.getImageUrl()));
            }
        }

        //조회수 증가
        store.increaseViewCnt();

        return new DetailStoreResDto(store, memberImageDto, totalImageDtoList, principalId);
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

        Page<Object[]> results = storeRepository.getAllStoreList(pageRequestDto.getSearchType(), pageRequestDto.getKeyword(), pageable);

        Function<Object[], AdminStoreResDto> fn = (arr -> {

            Store store = (Store) arr[0];
            int reviewCnt = (int) arr[1];

            ImageDto imageDto = null;

            if (store.getStoreImageList() != null && !store.getStoreImageList().isEmpty()) {

                StoreImage storeImage = store.getStoreImageList().get(0);

                imageDto = new ImageDto(storeImage.getImageId(), storeImage.getImageUrl());
            }

            return new AdminStoreResDto(store, reviewCnt, (Double) arr[2], imageDto);
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


        //storeImageDtoList
        List<ImageDto> storeImageDtoList = new ArrayList<>();

        //store 이미지 리스트
        if(store.getStoreImageList() != null && !store.getStoreImageList().isEmpty()){
            for (StoreImage storeImage : store.getStoreImageList()){
                storeImageDtoList.add(new ImageDto(storeImage.getImageId(), storeImage.getImageUrl()));
            }
        }

        return new AdminDetailStoreResDto(store, (int)arr[1], (long) arr[2], (int)arr[3], storeImageDtoList);
    }
}

