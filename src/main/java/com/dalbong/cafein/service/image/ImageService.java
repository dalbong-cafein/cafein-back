package com.dalbong.cafein.service.image;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.event.Event;
import com.dalbong.cafein.domain.image.BoardImage;
import com.dalbong.cafein.domain.image.Image;
import com.dalbong.cafein.domain.image.ReviewImage;
import com.dalbong.cafein.domain.image.StoreImage;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.store.Store;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ImageService {

    Image saveMemberImage(Member member, MultipartFile imageFile) throws IOException;

    List<StoreImage> saveStoreImage(Store store, Member regMember, List<MultipartFile> imageFiles, boolean isCafein) throws IOException;

    StoreImage saveStoreImage(Store store, File imageFile);

    List<ReviewImage> saveReviewImage(Review review, List<MultipartFile> imageFiles) throws IOException;

    List<BoardImage> saveBoardImage(Board board, List<MultipartFile> imageFiles) throws IOException;

    BoardImage saveBoardImage(Board board, MultipartFile imageFile) throws IOException;

    Image saveEventImage(Event event, MultipartFile imageFile) throws IOException;

    void remove(Long imageId);

    void setUpRepresentativeImage(Long storeId, Long ImageId);
}
