package com.dalbong.cafein.dto.businessHours;

import com.dalbong.cafein.domain.businessHours.Day;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BusinessHoursUpdateDto {

    private Day onMon;

    private Day onTue;

    private Day onWed;

    private Day onThu;

    private Day onFri;

    private Day onSat;

    private Day onSun;

    private String etcTime = "";

}
