package com.dalbong.cafein.domain.review;

import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.businessHours.Day;
import com.dalbong.cafein.domain.image.ReviewImage;
import com.dalbong.cafein.domain.image.StoreImage;
import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.store.StoreRegDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ReviewJdbcTemplateRepositoryImplTest {

    @Autowired ReviewRepository reviewRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired StoreRepository storeRepository;

    private Member member;

    @BeforeEach
    void before(){
        Member member = createMember("testUsername", "testNickname", "010-1111-1111",
                "testEmail@naver.com", "asdf123", LocalDate.now());
        this.member = member;
    }

    @Test
    void jdbcTemplate_test() throws Exception{
        //given

        //store 생성

        //주소생성
        Address address = createAddress("서울특별시", "종로구", "새문안로", "85", null);

        //영업시간
        BusinessHours businessHours = createBusinessHours(new Day(LocalTime.of(07, 00), LocalTime.of(23, 00)),
                new Day(LocalTime.of(07, 00), LocalTime.of(23, 00)),
                new Day(LocalTime.of(07, 00), LocalTime.of(23, 00)),
                new Day(LocalTime.of(07, 00), LocalTime.of(23, 00)),
                new Day(LocalTime.of(07, 00), LocalTime.of(23, 00)),
                new Day(LocalTime.of(07, 00), LocalTime.of(23, 00)),
                new Day(LocalTime.of(07, 00), LocalTime.of(23, 00)));


        List<MultipartFile> imageFiles = new ArrayList<>();
        MultipartFile file1 = createImage("file1", "testImage1.jpeg");
        MultipartFile file2 = createImage("file2", "testImage2.jpeg");
        imageFiles.add(file1);
        imageFiles.add(file2);

        Store store = createStore("store1", address, "010-0000-0000", "cafeinofficial.com",
                imageFiles, 37.1, 127.1, businessHours, member);

        System.out.println("------------");
        System.out.println(store.getStoreId());


        //review 생성
        DetailEvaluation detailEvaluation = new DetailEvaluation(1, 2, 3, 4);

        Review review = createReview(member, store, Recommendation.GOOD, detailEvaluation, "testContent");


        //when
        System.out.println("--------------------");
        List<ReviewMappedDto> reviewMappedDtoList = reviewRepository.getMultiEntity(1L);
        System.out.println("-------------------------");

        Store store1 = storeRepository.findById(store.getStoreId()).get();
        List<StoreImage> storeImageList1 = store1.getStoreImageList();
        System.out.println(storeImageList1.size());

        //then
        for (ReviewMappedDto reviewMappedDto : reviewMappedDtoList){
            Review findReview = reviewMappedDto.getReview();
            findReview.getReviewImageList();
            System.out.println(findReview.toString());
            System.out.println(findReview.getRecommendation());
            System.out.println(findReview.getReviewId());


            Store findStore = reviewMappedDto.getStore();
            List<StoreImage> storeImageList = findStore.getStoreImageList();
            System.out.println(findStore);
            System.out.println(storeImageList.size());

        }
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

    private BusinessHours createBusinessHours(Day onMon, Day onTue, Day onWed, Day onThu,
                                              Day onFri, Day onSun, Day onSat) {

        return BusinessHours.builder()
                .onMon(onMon).onTue(onTue).onWed(onWed).onThu(onThu)
                .onFri(onFri).onSun(onSun).onSat(onSat)
                .build();
    }


    private Address createAddress(String siNm, String sggNm, String rNm, String rNum, String detail) {
        return new Address(siNm,sggNm,rNm, rNum, detail);
    }

    private MultipartFile createImage(String name, String originalFilename) {
        return new MockMultipartFile(name, originalFilename, "image/jpeg", "some-image".getBytes());
    }

    private Store createStore(String storeName, Address address, String phone,
                                          String website, List<MultipartFile> imageFiles, double lngX, double latY,
                                          BusinessHours businessHours, Member member) {
        Store store = Store.builder()
                .regMember(member)
                .modMember(member)
                .storeName(storeName)
                .address(address)
                .lngX(lngX).latY(latY)
                .phone(phone)
                .website(website)
                .businessHours(businessHours)
                .build();

        return storeRepository.save(store);
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