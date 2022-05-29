package com.dalbong.cafein.service.store;

import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.businessHours.BusinessHoursRepository;
import com.dalbong.cafein.domain.businessHours.Day;
import com.dalbong.cafein.domain.image.StoreImage;
import com.dalbong.cafein.domain.image.StoreImageRepository;
import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.review.ReviewRepository;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import com.dalbong.cafein.dto.store.StoreRegDto;
import org.aspectj.lang.annotation.RequiredTypes;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class StoreServiceImplTest {

    @Autowired StoreService storeService;
    @Autowired StoreRepository storeRepository;
    @Autowired BusinessHoursRepository businessHoursRepository;
    @Autowired StoreImageRepository storeImageRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired ReviewRepository reviewRepository;

    private Member member;

    @BeforeEach
    void before(){
        Member member = createMember("testUsername", "testNickname", "010-1111-1111",
                "testEmail@naver.com", "asdf123", LocalDate.now());
        this.member = member;
    }


    /**
     * 카페 등록
     */
    @Test
    void 카페등록() throws Exception{
        //given

        List<MultipartFile> imageFiles = new ArrayList<>();
        MultipartFile imageFile1 = createImage("testStoreName1", "createStoreFilename1.png");
        MultipartFile imageFile2 = createImage("testStoreName2", "createStoreFilename2.png");
        imageFiles.add(imageFile1); imageFiles.add(imageFile2);

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

        //storeRegDto 생성
        StoreRegDto storeRegDto = createStoreRegDto("testStoreName", address, "02-000-0000", "test@cafeinofficial.com",
             imageFiles, 37.01654, 127.1067896, businessHours, Recommendation.GOOD,
                1,3,4,1);

        //when
        Store store = storeService.register(storeRegDto, member.getMemberId());

        //then

        //store 검증
        assertThat(store.getStoreId()).isNotNull();
        assertThat(store.getRegMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(store.getModMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(store.getStoreName()).isEqualTo(storeRegDto.getStoreName());
        assertThat(store.getAddress().toString()).isEqualTo(address.toString());
        assertThat(store.getPhone()).isEqualTo(storeRegDto.getPhone());
        assertThat(store.getWebsite()).isEqualTo(storeRegDto.getWebsite());
        assertThat(store.getLngX()).isEqualTo(storeRegDto.getLngX());
        assertThat(store.getLatY()).isEqualTo(storeRegDto.getLatY());

        //영업시간 검증
        BusinessHours findBusinessHours = businessHoursRepository.findById(store.getBusinessHours().getBusinessHoursId()).get();
        assertThat(findBusinessHours.getOnMon().toString()).isEqualTo(businessHours.getOnMon().toString());
        assertThat(findBusinessHours.getOnTue().toString()).isEqualTo(businessHours.getOnTue().toString());
        assertThat(findBusinessHours.getOnWed().toString()).isEqualTo(businessHours.getOnWed().toString());
        assertThat(findBusinessHours.getOnThu().toString()).isEqualTo(businessHours.getOnThu().toString());
        assertThat(findBusinessHours.getOnFri().toString()).isEqualTo(businessHours.getOnFri().toString());
        assertThat(findBusinessHours.getOnSat().toString()).isEqualTo(businessHours.getOnSat().toString());
        assertThat(findBusinessHours.getOnSun().toString()).isEqualTo(businessHours.getOnSun().toString());

        //storeImage 검증
        List<StoreImage> storeImageList = storeImageRepository.findByStore(store);
        assertThat(storeImageList.size()).isEqualTo(2);

        //review 검증
        List<Review> findReviewList = reviewRepository.findByStore(store);
        assertThat(findReviewList.size()).isEqualTo(1);

        Review findReview = findReviewList.get(0);
        assertThat(findReview.getMember().getMemberId()).isEqualTo(member.getMemberId());

        System.out.println(store);
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

    private StoreRegDto createStoreRegDto(String storeName, Address address, String phone,
                                          String website, List<MultipartFile> imageFiles, double lngX, double latY,
                                          BusinessHours businessHours, Recommendation recommendation,
                                          int socket, int wifi, int restroom, int tableSize) {

        return StoreRegDto.builder()
                .storeName(storeName)
                .siNm(address.getSiNm()).sggNm(address.getSggNm())
                .rNm(address.getRNm()).rNum(address.getRNum()).detail(address.getDetail())
                .phone(phone)
                .website(website)
                .imageFiles(imageFiles)
                .lngX(lngX).latY(latY)
                .monOpen(businessHours.getOnMon().getOpen()).monClosed(businessHours.getOnMon().getClosed())
                .tueOpen(businessHours.getOnTue().getOpen()).tueClosed(businessHours.getOnTue().getClosed())
                .wedOpen(businessHours.getOnWed().getOpen()).wedClosed(businessHours.getOnWed().getClosed())
                .thuOpen(businessHours.getOnThu().getOpen()).thuClosed(businessHours.getOnThu().getClosed())
                .friOpen(businessHours.getOnFri().getOpen()).friClosed(businessHours.getOnFri().getClosed())
                .satOpen(businessHours.getOnSat().getOpen()).satClosed(businessHours.getOnSat().getClosed())
                .sunOpen(businessHours.getOnSun().getOpen()).sunClosed(businessHours.getOnSun().getClosed())
                .recommendation(recommendation)
                .socket(socket).wifi(wifi).restroom(restroom).tableSize(tableSize)
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