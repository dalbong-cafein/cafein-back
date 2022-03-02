package com.dalbong.cafein.dto.businessHours;

import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.businessHours.Day;
import com.dalbong.cafein.domain.store.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BusinessHoursRegDto {

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

    public BusinessHours toEntity(Long storeId){
        return BusinessHours.builder()
                .store(Store.builder().storeId(storeId).build())
                .onMon(new Day(monOpen, monClosed))
                .onTue(new Day(tueOpen, tueClosed))
                .onWed(new Day(wedOpen, wedClosed))
                .onThu(new Day(thuOpen, thuClosed))
                .onFri(new Day(friOpen, friClosed))
                .onSat(new Day(satOpen, satClosed))
                .onSun(new Day(sunOpen, sunClosed))
                .build();
    }
}