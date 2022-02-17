package com.dalbong.cafein.domain.businessHours;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable //jpa 내장타입
@Getter
public class Day {

    private LocalDateTime open;

    private LocalDateTime closed;
}
