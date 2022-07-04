package com.dalbong.cafein.service.image;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.image.*;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.admin.eventImage.AdminEventImageListResDto;
import com.dalbong.cafein.dto.admin.eventImage.AdminEventImageResDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewListResDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewResDto;
import com.dalbong.cafein.dto.image.ImageDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.page.PageResultDTO;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Transactional
@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService{

    private final MemberImageRepository memberImageRepository;
    private final StoreImageRepository storeImageRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final BoardImageRepository boardImageRepository;
    private final EventImageRepository eventImageRepository;
    private final ImageRepository imageRepository;
    private final S3Uploader s3Uploader;

    /**
     * 프로필 이미지 저장
     */
    @Transactional
    @Override
    public Image saveMemberImage(Member member, MultipartFile imageFile) throws IOException {

        //s3 업로드
        String imageUrl = s3Uploader.s3UploadOfProfileImage(member, imageFile);

        //Image 저장
        MemberImage memberImage = new MemberImage(member, imageUrl);
        return memberImageRepository.save(memberImage);
    }

    /**
     * 카페 이미지 저장
     */
    @Transactional
    @Override
    public List<StoreImage> saveStoreImage(Store store, List<MultipartFile> imageFiles) throws IOException {

        List<String> imageUrlList = s3Uploader.s3MultipleUploadOfStore(store, imageFiles);

        List<StoreImage> imageList = new ArrayList<>();

        //StoreImage 저장
        if(!imageUrlList.isEmpty()){
            for(String imageUrl : imageUrlList){
                StoreImage storeImage =
                        storeImageRepository.save(new StoreImage(store, imageUrl));
                imageList.add(storeImage);
            }
        }

        return imageList;
    }


    /**
     * 리뷰 이미지 저장
     */
    @Transactional
    @Override
    public List<ReviewImage> saveReviewImage(Review review, List<MultipartFile> imageFiles) throws IOException {

        //s3업로드
        List<String> imageUrlList = s3Uploader.s3MultipleUploadOfReview(review, imageFiles);

        List<ReviewImage> imageList = new ArrayList<>();

        //ReviewImage 저장
        if(!imageUrlList.isEmpty()){
            for(String imageUrl : imageUrlList){
                ReviewImage reviewImage =
                        reviewImageRepository.save(new ReviewImage(review, imageUrl));
                imageList.add(reviewImage);
            }
        }
        return imageList;
    }

    /**
     * 게시글 이미지 저장
     */
    @Transactional
    @Override
    public List<BoardImage> saveBoardImage(Board board, List<MultipartFile> imageFiles) throws IOException {
        //s3업로드
        List<String> imageUrlList = s3Uploader.s3MultipleUploadOfBoard(board, imageFiles);

        List<BoardImage> imageList = new ArrayList<>();

        //ReviewImage 저장
        if(!imageUrlList.isEmpty()){
            for(String imageUrl : imageUrlList){
                BoardImage boardImage =
                        boardImageRepository.save(new BoardImage(board, imageUrl));
                imageList.add(boardImage);
            }
        }
        return imageList;
    }

    /**
     * 이벤트 이미지 저장
     */
    @Transactional
    @Override
    public Image saveEventImage(MultipartFile imageFile) throws IOException {

        //s3업로드
        String eventImageUrl = s3Uploader.s3UploadOfEvent(imageFile);

        //eventImage 저장
        EventImage eventImage = new EventImage(eventImageUrl);

        return eventImageRepository.save(eventImage);
    }

    /**
     * 관리자단 이벤트 이미지 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminEventImageListResDto<?> getEventImageListOfAdmin(PageRequestDto pageRequestDto) {

        Pageable pageable;

        if(pageRequestDto.getSort().equals("ASC")){
            pageable = pageRequestDto.getPageable(Sort.by("imageId").ascending());
        }else{
            pageable = pageRequestDto.getPageable(Sort.by("imageId").descending());
        }

        Page<EventImage> results = imageRepository.getEventImageList(pageable);

        Function<EventImage, AdminEventImageResDto> fn = (AdminEventImageResDto::new);


        return new AdminEventImageListResDto<>(results.getTotalElements(), new PageResultDTO<>(results, fn));
    }

    /**
     * 이미지 삭제
     */
    @Transactional
    @Override
    public void remove(Long imageId){

        System.out.println(imageId);
        //s3 이미지 파일 삭제
        s3Uploader.delete(imageId);

        System.out.println("-------------");

        try{
            imageRepository.deleteById(imageId);
        }catch (EmptyResultDataAccessException e){
            throw new CustomException("존재하는 이미지가 없습니다.");
        }

    }
}
