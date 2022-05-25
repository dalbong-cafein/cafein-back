package com.dalbong.cafein.dto.store;

import com.dalbong.cafein.domain.address.Address;
import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.businessHours.Day;
import com.dalbong.cafein.dto.businessHours.BusinessHoursUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StoreUpdateDto {

    private Long storeId;

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

    //이미지 추가
    private List<MultipartFile> updateImageFiles = new ArrayList<>();

    //이미지 삭제
    private List<Long> deleteImageIdList = new ArrayList<>();

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

    public Address getAddress(){
        return new Address(siNm, sggNm, rNm, rNum, detail);
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

    public BusinessHoursUpdateDto toBusinessHoursUpdateDto(){

        return BusinessHoursUpdateDto.builder()
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

}
