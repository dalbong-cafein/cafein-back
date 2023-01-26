package com.dalbong.cafein.domain.businessHours;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalTime;

@ToString
@Embeddable //jpa 내장타입
@Getter
public class Day {

    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime open;

    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "Asia/Seoul")
    private LocalTime closed;

    @Enumerated(EnumType.STRING)
    private HolidayType holidayType;

    protected Day(){}

    public Day(LocalTime open, LocalTime closed, HolidayType holidayType) {
        this.open = open;
        this.closed = closed;
        this.holidayType = holidayType;
    }

    public Day(LocalTime open, LocalTime closed){
        this(open, closed, null);
    }
}
