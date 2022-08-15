package com.dalbong.cafein.dataSet.store;

import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.businessHours.Day;
import com.dalbong.cafein.domain.store.Store;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class GoogleStoreDto {

    public GoogleStoreDto(String name, String phone, Double lngX, Double latY,
                          List<Map<String,Object>> daysOpening, List<String> photoReferenceList, String formattedAddress){

        this.storeName = name;
        this.phone = phone;
        this.lngX = lngX;
        this.latY = latY;
        this.formattedAddress = formattedAddress;

        //open, closed
        LocalTime monOpen = null, monClosed = null,tueOpen= null, tueClosed= null, wedOpen= null, wedClosed= null,
                thuOpen= null, thuClosed= null, friOpen= null, friClosed= null,satOpen= null, satClosed= null,
                sunOpen = null, sunClosed= null;

        if(daysOpening != null){


            for (Map<String,Object> arr : daysOpening){

                Map<String,Object> close = (Map<String,Object>) arr.get("close");
                Map<String,Object> open = (Map<String,Object>) arr.get("open");

                //요일별 영업시간 데이터가 없을 경우
                if(close != null){
                    switch ((Integer)close.get("day")){
                        case 0:
                            String sunOpenTime = (String) open.get("time");
                            String sunCloseTime = (String) close.get("time");
                            sunOpen = LocalTime.parse(sunOpenTime,DateTimeFormatter.ofPattern("HHmm"));
                            sunClosed = LocalTime.parse(sunCloseTime,DateTimeFormatter.ofPattern("HHmm"));
                            break;
                        case 1:
                            String monOpenTime = (String) open.get("time");
                            String monCloseTime = (String) close.get("time");
                            monOpen = LocalTime.parse(monOpenTime,DateTimeFormatter.ofPattern("HHmm"));
                            monClosed = LocalTime.parse(monCloseTime,DateTimeFormatter.ofPattern("HHmm"));
                            break;
                        case 2:
                            String tueOpenTime = (String) open.get("time");
                            String tueCloseTime = (String) close.get("time");
                            tueOpen = LocalTime.parse(tueOpenTime,DateTimeFormatter.ofPattern("HHmm"));
                            tueClosed = LocalTime.parse(tueCloseTime,DateTimeFormatter.ofPattern("HHmm"));
                            break;
                        case 3:
                            String wedOpenTime = (String) open.get("time");
                            String wedCloseTime = (String) close.get("time");
                            wedOpen = LocalTime.parse(wedOpenTime,DateTimeFormatter.ofPattern("HHmm"));
                            wedClosed = LocalTime.parse(wedCloseTime,DateTimeFormatter.ofPattern("HHmm"));
                            break;
                        case 4:
                            String thuOpenTime = (String) open.get("time");
                            String thuCloseTime = (String) close.get("time");
                            thuOpen = LocalTime.parse(thuOpenTime,DateTimeFormatter.ofPattern("HHmm"));
                            thuClosed = LocalTime.parse(thuCloseTime,DateTimeFormatter.ofPattern("HHmm"));
                            break;
                        case 5:
                            String friOpenTime = (String) open.get("time");
                            String friCloseTime = (String) close.get("time");
                            friOpen = LocalTime.parse(friOpenTime,DateTimeFormatter.ofPattern("HHmm"));
                            friClosed = LocalTime.parse(friCloseTime,DateTimeFormatter.ofPattern("HHmm"));
                            break;
                        case 6:
                            String satOpenTime = (String) open.get("time");
                            String satCloseTime = (String) close.get("time");
                            satOpen = LocalTime.parse(satOpenTime,DateTimeFormatter.ofPattern("HHmm"));
                            satClosed = LocalTime.parse(satCloseTime,DateTimeFormatter.ofPattern("HHmm"));
                            break;
                    }
                }
            }
        }

        if(daysOpening != null) {
            this.businessHours = BusinessHours.builder()
                    .onMon(new Day(monOpen, monClosed))
                    .onTue(new Day(tueOpen, tueClosed))
                    .onWed(new Day(wedOpen, wedClosed))
                    .onThu(new Day(thuOpen, thuClosed))
                    .onFri(new Day(friOpen, friClosed))
                    .onSat(new Day(satOpen, satClosed))
                    .onSun(new Day(sunOpen, sunClosed))
                    .build();
        }
        this.photoReferenceList = photoReferenceList;
    }

    private String storeName;

    private String phone;

    private BusinessHours businessHours;

    private Double lngX;

    private Double latY;

    private List<String> photoReferenceList;

    private String formattedAddress;
}
