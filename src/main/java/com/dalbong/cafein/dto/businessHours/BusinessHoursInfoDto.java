package com.dalbong.cafein.dto.businessHours;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@Data
public class BusinessHoursInfoDto {

    private Boolean isOpen;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime open;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime closed;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime tmrOpen;

    public BusinessHoursInfoDto(Map<String,Object> businessHoursInfoMap){

        this.isOpen = (Boolean) businessHoursInfoMap.get("isOpen");
        this.open = (LocalTime) businessHoursInfoMap.get("open");
        this.closed = (LocalTime) businessHoursInfoMap.get("closed");
        this.tmrOpen = (LocalTime) businessHoursInfoMap.get("tmrOpen");
    }

}
