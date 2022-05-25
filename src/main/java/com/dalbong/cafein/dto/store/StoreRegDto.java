package com.dalbong.cafein.dto.store;

import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.businessHours.Day;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.dto.review.ReviewRegDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StoreRegDto {

    private String storeName;

    private String siNm; //시도명
    private String sggNm; //시군구
    private String rNm; //도로명
    private String rNum; //도로 number
    private String detail; //상세주소

    private String phone;

    private String wifiPassword;

    private String website;

    private Integer katechX;

    private Integer katechY;

    private List<MultipartFile> imageFiles = new ArrayList<>();

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime monOpen;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime monClosed;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime tueOpen;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime tueClosed;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime wedOpen;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime wedClosed;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime thuOpen;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime thuClosed;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime friOpen;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime friClosed;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime satOpen;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime satClosed;

    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime sunOpen;
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime sunClosed;

    private String etcTime;

    //카페 평가 데이터
    private Recommendation recommendation;

    @Max(value = 4) @Min(value = 1)
    private int socket;

    @Max(value = 4) @Min(value = 1)
    private int wifi;

    @Max(value = 4) @Min(value = 1)
    private int restroom;

    @Max(value = 4) @Min(value = 1)
    private int tableSize;

    public Store toEntity(Long principalId){

        Address address = new Address(siNm, sggNm, rNm, rNum, detail);

        return Store.builder()
                .regMember(Member.builder().memberId(principalId).build())
                .modMember(Member.builder().memberId(principalId).build())
                .storeName(storeName)
                .address(address)
                .katechX(katechX).katechY(katechY)
                .phone(phone)
                .wifiPassword(wifiPassword)
                .website(website)
                .build();
    }

    public BusinessHours toBusinessHoursEntity(){

        return BusinessHours.builder()
                .onMon(new Day(monOpen, monClosed))
                .onTue(new Day(tueOpen, tueClosed))
                .onWed(new Day(wedOpen, wedClosed))
                .onThu(new Day(thuOpen, thuClosed))
                .onFri(new Day(friOpen, friClosed))
                .onSat(new Day(satOpen, satClosed))
                .onSun(new Day(sunOpen, sunClosed))
                .etcTime(etcTime)
                .build();
    }

    public ReviewRegDto toReviewRegDto(Long storeId){

        return ReviewRegDto.builder()
                .storeId(storeId)
                .recommendation(recommendation)
                .socket(socket).wifi(wifi).restroom(restroom).tableSize(tableSize)
           //     .imageFiles(imageFiles)
                .build();

    }
}
