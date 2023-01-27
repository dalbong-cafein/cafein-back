package com.dalbong.cafein.domain.businessHours;

import com.dalbong.cafein.domain.store.Store;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Entity
public class BusinessHours {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long businessHoursId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="open", column = @Column(name="mon_open")),
            @AttributeOverride(name="closed", column = @Column(name="mon_closed")),
            @AttributeOverride(name="holidayType", column = @Column(name="mon_holiday_type"))
    })
    private Day onMon;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="open", column = @Column(name="tue_open")),
            @AttributeOverride(name="closed", column = @Column(name="tue_closed")),
            @AttributeOverride(name="holidayType", column = @Column(name="tue_holiday_type"))
    })
    private Day onTue;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="open", column = @Column(name="wed_open")),
            @AttributeOverride(name="closed", column = @Column(name="wed_closed")),
            @AttributeOverride(name="holidayType", column = @Column(name="wed_holiday_type"))
    })
    private Day onWed;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="open", column = @Column(name="thu_open")),
            @AttributeOverride(name="closed", column = @Column(name="thu_closed")),
            @AttributeOverride(name="holidayType", column = @Column(name="thu_holiday_type"))
    })
    private Day onThu;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="open", column = @Column(name="fri_open")),
            @AttributeOverride(name="closed", column = @Column(name="fri_closed")),
            @AttributeOverride(name="holidayType", column = @Column(name="fri_holiday_type"))
    })
    private Day onFri;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="open", column = @Column(name="sat_open")),
            @AttributeOverride(name="closed", column = @Column(name="sat_closed")),
            @AttributeOverride(name="holidayType", column = @Column(name="sat_holiday_type"))
    })
    private Day onSat;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="open", column = @Column(name="sun_open")),
            @AttributeOverride(name="closed", column = @Column(name="sun_closed")),
            @AttributeOverride(name="holidayType", column = @Column(name="sun__holiday_type"))
    })
    private Day onSun;

    private String etcTime;

    public void changeOnMon(Day onMon){
        this.onMon = onMon;
    }
    public void changeOnTue(Day onTue){
        this.onTue = onTue;
    }
    public void changeOnWed(Day onWed){
        this.onWed = onWed;
    }
    public void changeOnThu(Day onThu){
        this.onThu = onThu;
    }
    public void changeOnFri(Day onFri){
        this.onFri = onFri;
    }
    public void changeOnSat(Day onSat){
        this.onSat = onSat;
    }
    public void changeOnSun(Day onSun){
        this.onSun = onSun;
    }
    public void changeEtcTime(String etcTime){
        this.etcTime = etcTime;
    }

}
