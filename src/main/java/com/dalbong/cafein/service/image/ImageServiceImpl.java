package com.dalbong.cafein.service.image;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.event.Event;
import com.dalbong.cafein.domain.image.*;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    public List<StoreImage> saveStoreImage(Store store, Member regMember, List<MultipartFile> imageFiles, boolean isCafein) throws IOException {

        List<String> imageUrlList = s3Uploader.s3MultipleUploadOfStore(store, imageFiles);

        List<StoreImage> imageList = new ArrayList<>();

        //StoreImage 저장
        if(!imageUrlList.isEmpty()){
            for(String imageUrl : imageUrlList){
                StoreImage storeImage =
                        storeImageRepository.save(new StoreImage(store, regMember, imageUrl, isCafein));
                imageList.add(storeImage);
            }
        }

        return imageList;
    }

    /**
     * 로컬 카페 이미지 저장
     */
    @Transactional
    @Override
    public StoreImage saveStoreImage(Store store, File imageFile) {

        String imageUrl = s3Uploader.s3UploadOfStore(store, imageFile);

        return storeImageRepository.save(new StoreImage(store, Member.builder().memberId(1L).build(), imageUrl, true));
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
                        reviewImageRepository.save(new ReviewImage(review,imageUrl));
                imageList.add(reviewImage);
            }
        }
        return imageList;
    }


    /**
     * 게시글 이미지 리스트 저장
     */
    @Transactional
    @Override
    public List<BoardImage> saveBoardImage(Board board, List<MultipartFile> imageFiles) throws IOException {
        //s3업로드
        List<String> imageUrlList = s3Uploader.s3MultipleUploadOfBoard(board, imageFiles);

        List<BoardImage> imageList = new ArrayList<>();

        //boardImage 저장
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
     * 게시글 이미지 저장
     */
    @Override
    public BoardImage saveBoardImage(Board board, MultipartFile imageFile) throws IOException {

        String imageUrl = s3Uploader.s3UploadOfBoard(board, imageFile);

        BoardImage boardImage = new BoardImage(board, imageUrl);

        return boardImageRepository.save(boardImage);
    }

    /**
     * 이벤트 이미지 저장
     */
    @Transactional
    @Override
    public Image saveEventImage(Event event, MultipartFile imageFile) throws IOException {

        //s3업로드
        String eventImageUrl = s3Uploader.s3UploadOfEvent(imageFile);

        //eventImage 저장
        EventImage eventImage = new EventImage(event, eventImageUrl);

        return eventImageRepository.save(eventImage);
    }

    /**
     * 이미지 삭제
     */
    @Transactional
    @Override
    public void remove(Long imageId){

        //s3 이미지 파일 삭제
        s3Uploader.delete(imageId);

        try{
            imageRepository.deleteById(imageId);
        }catch (EmptyResultDataAccessException e){
            throw new CustomException("존재하는 이미지가 없습니다.");
        }

    }

    /**
     * 카페 대표 이미지 조회
     */
    @Transactional
    @Override
    public Image getRepresentImageOfStore(Long storeId) {

        Long representImageId = imageRepository.getRepresentativeImageOfStore(storeId);

        if (representImageId != null) {
            return imageRepository.findById(representImageId).orElseThrow(() ->
                    new CustomException("존재하지 않는 이미지입니다."));
        }

        return null;
    }
}
