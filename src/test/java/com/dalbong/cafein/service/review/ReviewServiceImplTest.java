package com.dalbong.cafein.service.review;

import com.dalbong.cafein.domain.image.ImageRepository;
import com.dalbong.cafein.domain.image.ReviewImage;
import com.dalbong.cafein.domain.image.ReviewImageRepository;
import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.dto.review.ReviewRegDto;
import com.dalbong.cafein.service.image.ImageService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ReviewServiceImplTest {

    @Autowired ReviewService reviewService;
    @Autowired MemberRepository memberRepository;
    @Autowired ImageRepository imageRepository;
    @Autowired ReviewImageRepository reviewImageRepository;

    private Member member;

    @BeforeEach
    void before(){
        Member member = createMember("testUsername", "testNickname", "010-1111-1111",
                "testEmail@naver.com", "asdf123", LocalDate.now());
        this.member = member;
    }

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

    @Test
    void 리뷰등록_사진o() throws Exception{
        //given
        //TODO store 생성 (아직 store 완성x)

        List<MultipartFile> imgFiles = new ArrayList<>();
        MultipartFile file1 = createImage("file1", "filename-1.jpeg");
        MultipartFile file2 = createImage("file2", "filename-2.jpeg");
        imgFiles.add(file1);
        imgFiles.add(file2);

        ReviewRegDto reviewRegDto
                = createReviewRegDto(1L, "testContent", Recommendation.GOOD,
                1, 2, 3, 5, imgFiles);

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
                                String oauthId, LocalDate birth) {

        Member member = Member.builder()
                .username(username)
                .nickname(nickname)
                .phone(phone)
                .email(email)
                .password("1111")
                .birth(birth)
                .oauthId(oauthId)
                .provider(AuthProvider.KAKAO)
                .build();

        return memberRepository.save(member);
    }

}