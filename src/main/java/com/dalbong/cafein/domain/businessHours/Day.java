package com.dalbong.cafein.domain.businessHours;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.time.LocalTime;

@Embeddable //jpa 내장타입
@Getter
public class Day {

    private LocalTime open;

    private LocalTime closed;

    protected Day(){}

    public Day(LocalTime open, LocalTime closed) {
    }
}
