package com.dalbong.cafein.service.heart;

import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.businessHours.BusinessHoursRepository;
import com.dalbong.cafein.domain.businessHours.Day;
import com.dalbong.cafein.domain.heart.Heart;
import com.dalbong.cafein.domain.heart.HeartRepository;
import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.store.StoreRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class HeartServiceImplTest {

    @Autowired HeartService heartService;
    @Autowired MemberRepository memberRepository;
    @Autowired StoreRepository storeRepository;
    @Autowired HeartRepository heartRepository;
    @Autowired BusinessHoursRepository businessHoursRepository;

    private Member member;
    private Store store;

    @BeforeEach
    void before() {
        //회원 생성
        Member member = createMember("testUsername", "testNickname", "010-1111-1111",
                "testEmail@naver.com", "asdf123", LocalDate.now());
        this.member = member;

        //가게 생성
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

        Store store = createStore("testStoreName", address, businessHours, "test@dalbong.com",
                "02-000-0000", 123, 123, member);

        this.store =store;

    }

    /**
     * 내카페 등록
     */
    @Test
    void 내카페_등록() {
        //given
        //when
        Heart heart = heartService.heart(member.getMemberId(), store.getStoreId());

        //then
        Heart findHeart = heartRepository.findById(heart.getHeartId()).get();
        assertThat(findHeart.getHeartId()).isNotNull();
        assertThat(findHeart.getMember().getMemberId()).isEqualTo(member.getMemberId());
        assertThat(findHeart.getStore().getStoreId()).isEqualTo(store.getStoreId());
    }

    /**
     * 내카페 취소
     */
    @Test
    void 내카페_취소() {

        //given
        Heart heart = createHeart(member, store);

        //when
        heartService.cancelHeart(member.getMemberId(), store.getStoreId());

        //then
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> (heartRepository.findById(heart.getHeartId())).get());

        assertThat(e.getMessage()).isEqualTo("No value present");

    }

    private Heart createHeart(Member member, Store store) {

        Heart heart = Heart.builder().member(member).store(store).build();
        return heartRepository.save(heart);
    }


    private Store createStore(String storeName, Address address, BusinessHours businessHours,
                              String website, String phone, int katechX, int katechY,
                              Member member) {

        Store store = Store.builder()
                .storeName(storeName)
                .address(address)
                .businessHours(businessHours)
                .website(website)
                .phone(phone)
                .katechX(katechX).katechY(katechY)
                .regMember(member)
                .modMember(member)
                .build();

        return storeRepository.save(store);
    }

    private BusinessHours createBusinessHours(Day onMon, Day onTue, Day onWed, Day onThu,
                                              Day onFri, Day onSun, Day onSat) {

        BusinessHours businessHours = BusinessHours.builder()
                .onMon(onMon).onTue(onTue).onWed(onWed).onThu(onThu)
                .onFri(onFri).onSun(onSun).onSat(onSat)
                .build();
        return businessHoursRepository.save(businessHours);
    }


    private Address createAddress(String siNm, String sggNm, String rNm, String rNum, String detail) {
        return new Address(siNm,sggNm,rNm, rNum, detail);
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