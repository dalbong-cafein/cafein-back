package com.dalbong.cafein.service.review;

import com.dalbong.cafein.domain.image.Image;
import com.dalbong.cafein.domain.image.ImageRepository;
import com.dalbong.cafein.domain.image.ReviewImage;
import com.dalbong.cafein.domain.image.ReviewImageRepository;
import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.domain.review.DetailEvaluation;
import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.review.ReviewRepository;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.review.ReviewRegDto;
import com.dalbong.cafein.dto.review.ReviewUpdateDto;
import com.dalbong.cafein.service.image.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Transactional
@SpringBootTest
class ReviewServiceImplTest {

    @Autowired ReviewService reviewService;
    @Autowired ReviewRepository reviewRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ImageRepository imageRepository;
    @Autowired ReviewImageRepository reviewImageRepository;
    @Autowired ImageService imageService;

    private Member member;

    @BeforeEach
    void before(){
        Member member = createMember("testUsername", "testNickname", "010-1111-1111",
                "testEmail@naver.com", "asdf123", LocalDate.now());
        this.member = member;
    }

    @Disabled
    @Test
    void 리뷰등록_사진x() throws Exception{
        //given
        //TODO store 생성 (아직 store 완성x)
        ReviewRegDto reviewRegDto = createReviewRegDto(1L, "testContent", Recommendation.GOOD, 1, 2, 3, 5);

        //when
        Review review = reviewService.register(reviewRegDto, member.getMemberId());

        //then
        assertThat(review.getReviewId()).isNotNull();
        assertThat(review.getContent()).isEqualTo(reviewRegDto.getContent());
        assertThat(review.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(review.getStore().getStoreId()).isEqualTo(1L);
        assertThat(review.getRecommendation()).isEqualTo(reviewRegDto.getRecommendation());
        assertThat(review.getDetailEvaluation().getRestroom()).isEqualTo(reviewRegDto.getRestroom());
        assertThat(review.getDetailEvaluation().getSocket()).isEqualTo(reviewRegDto.getSocket());
        assertThat(review.getDetailEvaluation().getTableSize()).isEqualTo(reviewRegDto.getTableSize());
        assertThat(review.getDetailEvaluation().getWifi()).isEqualTo(reviewRegDto.getWifi());
    }

    @Disabled
    @Test
    void 리뷰등록_사진o() throws Exception{
        //given
        //TODO store 생성 (아직 store 완성x)

        List<MultipartFile> imgFiles = new ArrayList<>();
        MultipartFile file1 = createImage("file1", "filename-1.jpeg");
        MultipartFile file2 = createImage("file2", "filename-2.jpeg");
        imgFiles.add(file1);
        imgFiles.add(file2);

        DetailEvaluation detailEvaluation = new DetailEvaluation(1, 2, 3, 4);

        ReviewRegDto reviewRegDto
                = createReviewRegDto(1L, "testContent", Recommendation.GOOD,
                1, 2, 3, 4, imgFiles);

        //when
        Review review = reviewService.register(reviewRegDto, member.getMemberId());

        //then
        assertThat(review.getReviewId()).isNotNull();
        assertThat(review.getContent()).isEqualTo(reviewRegDto.getContent());
        assertThat(review.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(review.getStore().getStoreId()).isEqualTo(1L);
        assertThat(review.getRecommendation()).isEqualTo(reviewRegDto.getRecommendation());
        assertThat(review.getDetailEvaluation().getRestroom()).isEqualTo(reviewRegDto.getRestroom());
        assertThat(review.getDetailEvaluation().getSocket()).isEqualTo(reviewRegDto.getSocket());
        assertThat(review.getDetailEvaluation().getTableSize()).isEqualTo(reviewRegDto.getTableSize());
        assertThat(review.getDetailEvaluation().getWifi()).isEqualTo(reviewRegDto.getWifi());

        //리뷰 이미지 검증
        List<ReviewImage> result = reviewImageRepository.findReviewImagesByReviewReviewId(review.getReviewId());
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getReview()).isEqualTo(review);
        assertThat(result.get(1).getReview()).isEqualTo(review);

    }

    //리뷰 수정
    @Disabled
    @Test
    void 리뷰_수정() throws Exception{
        //given
        DetailEvaluation detailEvaluation = new DetailEvaluation(1, 2, 3, 4);

        //TODO store 생성 (아직 store 완성x)
        Review review = createReview(member, Store.builder().storeId(1L).build(), Recommendation.GOOD, detailEvaluation, "testContent");


        //mock 이미지 생성
        List<MultipartFile> imageFiles = new ArrayList<>();
        MultipartFile file1 = createImage("file1", "testImage1.jpeg");
        MultipartFile file2 = createImage("file2", "testImage2.jpeg");
        imageFiles.add(file1);
        imageFiles.add(file2);

        //리뷰 이미지 생성
        List<Image> imageList = createReviewImage(review, imageFiles);

        //mock update 이미지 생성
        List<MultipartFile> updateImageFiles = new ArrayList<>();
        MultipartFile updateFile1 = createImage("updateFile1", "updateTestImage1.jpeg");

        updateImageFiles.add(updateFile1);

        //ReviewUpdateDto 생성
        ReviewUpdateDto reviewUpdateDto = createReviewUpdateDto(review.getReviewId(), Recommendation.BAD,
                4, 3, 2, 1, "updateContent", updateImageFiles);

        //when
        reviewService.modify(reviewUpdateDto);

        //then
        Review findReview = reviewRepository.findById(review.getReviewId()).get();

        System.out.println(findReview);

        assertThat(findReview.getReviewId()).isEqualTo(reviewUpdateDto.getReviewId());
        assertThat(findReview.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(findReview.getStore().getStoreId()).isEqualTo(1L);
        assertThat(findReview.getRecommendation()).isEqualTo(reviewUpdateDto.getRecommendation());
        assertThat(findReview.getDetailEvaluation()).isEqualTo(reviewUpdateDto.getDetailEvaluation());
        assertThat(findReview.getContent()).isEqualTo(reviewUpdateDto.getContent());

        //리뷰 이미지 검증
        List<ReviewImage> result = reviewImageRepository.findReviewImagesByReviewReviewId(review.getReviewId());
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getReview()).isEqualTo(review);
    }
    
    @Disabled
    @Test
    void 리뷰삭제() throws Exception{
        //given
        DetailEvaluation detailEvaluation = new DetailEvaluation(1, 2, 3, 4);

        //TODO store 생성 (아직 store 완성x)
        Review review = createReview(member, Store.builder().storeId(1L).build(), Recommendation.GOOD, detailEvaluation, "testContent");

        //mock 이미지 생성
        List<MultipartFile> imageFiles = new ArrayList<>();
        MultipartFile file1 = createImage("file1", "testImage1.jpeg");
        MultipartFile file2 = createImage("file2", "testImage2.jpeg");
        imageFiles.add(file1);
        imageFiles.add(file2);

        List<Image> imageList = createReviewImage(review, imageFiles);

        //when
        reviewService.remove(review.getReviewId());

        //then
        //삭제한 review의 reviewImg1 레코드 검색 (Image, ReviewImage 삭제)
        NoSuchElementException e1 = assertThrows(NoSuchElementException.class,
                () -> (imageRepository.findById(imageList.get(0).getImageId())).get());

        NoSuchElementException e2 = assertThrows(NoSuchElementException.class,
                () -> (reviewImageRepository.findById(imageList.get(0).getImageId())).get());

        //삭제한 review의 reviewImg2 레코드 검색 (Image, ReviewImage 삭제)
        NoSuchElementException e3 = assertThrows(NoSuchElementException.class,
                () -> (imageRepository.findById(imageList.get(1).getImageId())).get());

        NoSuchElementException e4 = assertThrows(NoSuchElementException.class,
                () -> (reviewImageRepository.findById(imageList.get(1).getImageId())).get());

        //삭제한 review 레코드 검색
        NoSuchElementException e5 = assertThrows(NoSuchElementException.class,
                () -> (reviewRepository.findById(review.getReviewId())).get());

        assertThat(e1.getMessage()).isEqualTo("No value present");
        assertThat(e2.getMessage()).isEqualTo("No value present");
        assertThat(e3.getMessage()).isEqualTo("No value present");
        assertThat(e4.getMessage()).isEqualTo("No value present");
        assertThat(e5.getMessage()).isEqualTo("No value present");

    }

    private ReviewUpdateDto createReviewUpdateDto(Long reviewId, Recommendation recommendation,
                                                  int restroom, int socket, int tableSize, int wifi,
                                                  String content, List<MultipartFile> imageFiles) {

        return ReviewUpdateDto.builder()
                .reviewId(reviewId)
                .recommendation(recommendation)
                .restroom(restroom)
                .socket(socket)
                .tableSize(tableSize)
                .wifi(wifi)
                .content(content)
                .imageFiles(imageFiles)
                .build();
    }

    private List<Image> createReviewImage(Review review, List<MultipartFile> imageFiles) throws IOException {

        return imageService.saveReviewImage(review, imageFiles);
    }

    private Review createReview(Member member, Store store, Recommendation recommendation,
                                DetailEvaluation detailEvaluation, String content) throws IOException {

        Review review = Review.builder()
                .member(member)
                .store(store)
                .recommendation(recommendation)
                .detailEvaluation(detailEvaluation)
                .content(content)
                .build();

        return reviewRepository.save(review);

    }

    private MultipartFile createImage(String name, String originalFilename) {
        return new MockMultipartFile(name, originalFilename, "image/jpeg", "some-image".getBytes());
    }


    private ReviewRegDto createReviewRegDto(Long storeId, String content, Recommendation recommendation,
                                            int restroom, int socket, int tableSize, int wifi,
                                            List<MultipartFile> imageFiles) {
        return ReviewRegDto.builder()
                .storeId(storeId)
                .content(content)
                .recommendation(recommendation)
                .restroom(restroom)
                .socket(socket)
                .tableSize(tableSize)
                .wifi(wifi)
                .imageFiles(imageFiles)
                .build();
    }

    private ReviewRegDto createReviewRegDto(Long storeId, String content, Recommendation recommendation,
                                            int restroom, int socket, int tableSize, int wifi) {
        return ReviewRegDto.builder()
                .storeId(storeId)
                .content(content)
                .recommendation(recommendation)
                .restroom(restroom)
                .socket(socket)
                .tableSize(tableSize)
                .wifi(wifi)
                .build();
    }

    private Member createMember(String username, String nickname, String phone, String email,
                                String kakaoId, LocalDate birth) {

        Member member = Member.builder()
                .username(username)
                .nickname(nickname)
                .phone(phone)
                .email(email)
                .password("1111")
                .birth(birth)
                .kakaoId(kakaoId)
                .mainAuthProvider(AuthProvider.KAKAO)
                .build();

        return memberRepository.save(member);
    }

}