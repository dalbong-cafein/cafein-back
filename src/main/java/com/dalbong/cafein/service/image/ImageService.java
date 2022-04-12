package com.dalbong.cafein.service.image;

import com.dalbong.cafein.domain.image.Image;
import com.dalbong.cafein.domain.image.ReviewImage;
import com.dalbong.cafein.domain.image.StoreImage;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.store.Store;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    Image saveMemberImage(Member member, MultipartFile imageFile) throws IOException;

    List<StoreImage> saveStoreImage(Store store, List<MultipartFile> imageFiles) throws IOException;

    List<ReviewImage> saveReviewImage(Review review, List<MultipartFile> imageFiles) throws IOException;

    void remove(Long imageId);


}
