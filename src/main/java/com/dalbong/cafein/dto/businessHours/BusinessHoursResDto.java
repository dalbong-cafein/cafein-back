package com.dalbong.cafein.dto.businessHours;

import com.dalbong.cafein.domain.businessHours.BusinessHours;
import com.dalbong.cafein.domain.businessHours.Day;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BusinessHoursResDto {

    private Day onMon;

    private Day onTue;

    private Day onWed;

    private Day onThu;

    private Day onFri;

    private Day onSat;

    private Day onSun;

    private String etcTime;

    public BusinessHoursResDto(BusinessHours businessHours){
        this.onMon = businessHours.getOnMon();
        this.onTue = businessHours.getOnTue();
        this.onWed = businessHours.getOnWed();
        this.onThu = businessHours.getOnThu();
        this.onFri = businessHours.getOnFri();
        this.onSat = businessHours.getOnSat();
        this.onSun = businessHours.getOnSun();
        this.etcTime = businessHours.getEtcTime();
    }


}
