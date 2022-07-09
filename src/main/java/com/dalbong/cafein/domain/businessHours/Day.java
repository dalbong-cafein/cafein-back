package com.dalbong.cafein.domain.businessHours;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Embeddable;
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

    protected Day(){}

    public Day(LocalTime open, LocalTime closed) {
        this.open = open;
        this.closed = closed;
    }
}
