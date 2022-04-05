package com.dalbong.cafein.domain.businessHours;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.time.LocalTime;

@ToString
@Embeddable //jpa 내장타입
@Getter
public class Day {

    private LocalTime open;

    private LocalTime closed;

    protected Day(){}

    public Day(LocalTime open, LocalTime closed) {
        this.open = open;
        this.closed = closed;
    }
}
