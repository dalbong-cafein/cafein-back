package com.dalbong.cafein.dto.businessHours;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@Data
public class BusinessHoursInfoDto {

    //TODO 휴무, 정기휴무 상태 추가
    private Boolean isOpen;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime closed;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime nextOpen;

    public BusinessHoursInfoDto(Map<String,Object> businessHoursInfoMap){

        this.isOpen = (Boolean) businessHoursInfoMap.get("isOpen");
        this.closed = (LocalTime) businessHoursInfoMap.get("closed");
        this.nextOpen = (LocalTime) businessHoursInfoMap.get("nextOpen");
    }

}
