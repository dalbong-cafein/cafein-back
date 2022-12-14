package com.dalbong.cafein.domain.store;

import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.businessHours.BusinessHoursRepository;
import com.dalbong.cafein.domain.businessHours.Day;
import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.domain.review.ReviewRepository;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static com.dalbong.cafein.domain.store.QStore.store;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class StoreRepositoryImplTest {
    
    private final JPAQueryFactory queryFactory;
    private MemberRepository memberRepository;
    private StoreRepository storeRepository;
    private BusinessHoursRepository businessHoursRepository;

    private Member member;


    @Autowired
    public StoreRepositoryImplTest(EntityManager entityManager, MemberRepository memberRepository, StoreRepository storeRepository,
                                   BusinessHoursRepository businessHoursRepository) {
        this.queryFactory = new JPAQueryFactory(entityManager);
        this.memberRepository = memberRepository;
        this.storeRepository = storeRepository;
        this.businessHoursRepository = businessHoursRepository;
    }

    @BeforeEach
    void before(){
        Member member = createMember("testUsername", "testNickname", "010-1111-1111",
                "testEmail@naver.com", "asdf123", LocalDate.now());
        this.member = member;
    }

    @Test
    void sql_function_test() throws Exception{
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

       createStore("store1", address, "010-0000-0000", "cafeinofficial.com",
                imageFiles, 127.1,37.1, businessHours, member);

        //when

        NumberExpression<Double> distance = calculateDistance("37.1,127.1");

        NumberTemplate<Double> radiansLatY1 = Expressions.numberTemplate(Double.class, "function('radians',{0})", 37.1);

        List<Double> result = queryFactory.select(distance).from(store).fetch();

        //then
        for(Double num : result){
            System.out.println(num);
        }
    }

    private NumberExpression<Double> calculateDistance(String coordinate) {

        if(coordinate != null && !coordinate.isEmpty()) {
            String[] coordinateArr = coordinate.split(",");

            double latY = Double.parseDouble(coordinateArr[0]);
            double lngX = Double.parseDouble(coordinateArr[1]);

            return acos(cos(radians(store.latY))
                    .multiply(cos(radians(latY)))
                    .multiply(cos(radians(lngX).subtract(radians(store.lngX))))
                    .add(sin(radians(store.latY).multiply(sin(radians(latY))))))
                    .multiply(6371);
        }

        return null;
    }

    private NumberTemplate<Double> acos(Object num) {
        return Expressions.numberTemplate(Double.class, "function('acos',{0})", num);
    }

    private NumberTemplate<Double> sin(Object num) {
        return Expressions.numberTemplate(Double.class, "function('sin',{0})", num);
    }

    private NumberTemplate<Double> cos(Object num) {
        return Expressions.numberTemplate(Double.class, "function('cos',{0})", num);
    }

    private NumberTemplate<Double> radians(Object num) {
        return Expressions.numberTemplate(Double.class, "function('radians',{0})", num);
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