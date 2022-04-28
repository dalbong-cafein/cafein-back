package com.dalbong.cafein.service.store;

import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.businessHours.BusinessHoursRepository;
import com.dalbong.cafein.domain.image.StoreImage;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.admin.store.AdminStoreListDto;
import com.dalbong.cafein.dto.admin.store.AdminStoreResDto;
import com.dalbong.cafein.dto.image.ImageDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.page.PageResultDTO;
import com.dalbong.cafein.dto.store.StoreRegDto;
import com.dalbong.cafein.service.image.ImageService;
import com.dalbong.cafein.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.function.Function;

@Transactional
@RequiredArgsConstructor
@Service
public class StoreServiceImpl implements StoreService{

    private final StoreRepository storeRepository;
    private final BusinessHoursRepository businessHoursRepository;
    private final ImageService imageService;
    private final ReviewService reviewService;

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
     * 관리자단 카페 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminStoreListDto getStoreListOfAdmin(PageRequestDto pageRequestDto) {

        //TODO 동적 필요
        Pageable pageable = pageRequestDto.getPageable(Sort.by("storeId").descending());

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
}

