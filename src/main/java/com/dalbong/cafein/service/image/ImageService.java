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
import com.dalbong.cafein.dto.admin.eventImage.AdminEventImageListResDto;
import com.dalbong.cafein.dto.admin.eventImage.AdminEventImageResDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewResDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.page.PageResultDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {

    Image saveMemberImage(Member member, MultipartFile imageFile) throws IOException;

    List<StoreImage> saveStoreImage(Store store, List<MultipartFile> imageFiles) throws IOException;

    List<ReviewImage> saveReviewImage(Review review, List<MultipartFile> imageFiles) throws IOException;

    List<BoardImage> saveBoardImage(Board board, List<MultipartFile> imageFiles) throws IOException;

    Image saveEventImage(Event event, MultipartFile imageFile) throws IOException;

    AdminEventImageListResDto<?> getEventImageListOfAdmin(PageRequestDto pageRequestDto);

    void remove(Long imageId);
}
