package com.dalbong.cafein.service.image;

import com.dalbong.cafein.domain.image.*;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService{

    private final MemberImageRepository memberImageRepository;
    private final StoreImageRepository storeImageRepository;
    private final ReviewImageRepository reviewImageRepository;
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
     * 가게 이미지 저장
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
     * 이미지 삭제
     */
    @Transactional
    @Override
    public void remove(Long imageId){

        //s3 이미지 파일 삭제
        s3Uploader.delete(imageId);

        imageRepository.deleteById(imageId);
    }
}
