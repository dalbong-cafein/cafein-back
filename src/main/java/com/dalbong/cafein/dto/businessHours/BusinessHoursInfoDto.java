package com.dalbong.cafein.dto.businessHours;

import com.dalbong.cafein.domain.businessHours.HolidayType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@Data
public class BusinessHoursInfoDto {

    private Boolean isOpen;

    private HolidayType holidayType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime closed;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime nextOpen;

    public BusinessHoursInfoDto(Map<String,Object> businessHoursInfoMap){

        this.isOpen = (Boolean) businessHoursInfoMap.get("isOpen");
        this.holidayType = (HolidayType) businessHoursInfoMap.get("holidayType");
        this.closed = (LocalTime) businessHoursInfoMap.get("closed");
        this.nextOpen = (LocalTime) businessHoursInfoMap.get("nextOpen");
    }

}
