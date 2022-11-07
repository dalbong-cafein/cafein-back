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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    @NotBlank
    private String storeName;

    @NotBlank
    private String siNm; //시도명

    @NotBlank
    private String sggNm; //시군구

    @NotBlank
    private String rNm; //도로명

    @NotBlank
    private String rNum; //도로 number

    private String detail; //상세주소

    private String phone;

    private String wifiPassword;

    private String website;

    @NotNull
    private Double lngX;

    @NotNull
    private Double latY;

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
    @NotNull
    private Recommendation recommendation;

    @NotNull
    @Max(value = 5) @Min(value = 1)
    private int socket;

    @NotNull
    @Max(value = 5) @Min(value = 1)
    private int wifi;

    @NotNull
    @Max(value = 5) @Min(value = 1)
    private int restroom;

    @NotNull
    @Max(value = 5) @Min(value = 1)
    private int tableSize;

    public Address getAddress(){
        return new Address(this.siNm, this.sggNm, this.rNm, this.rNum, this.detail);
    }

    public Store toEntity(Long principalId){

        Address address = getAddress();

        return Store.builder()
                .regMember(Member.builder().memberId(principalId).build())
                .modMember(Member.builder().memberId(principalId).build())
                .storeName(storeName)
                .address(address)
                .lngX(lngX).latY(latY)
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
                .etcTime(this.etcTime)
                .build();
    }

    public ReviewRegDto toReviewRegDto(Long storeId){

        return ReviewRegDto.builder()
                .storeId(storeId)
                .recommendation(recommendation)
                .socket(socket).wifi(wifi).restroom(restroom).tableSize(tableSize)
                .build();

    }
}
