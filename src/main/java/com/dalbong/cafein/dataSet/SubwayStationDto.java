package com.dalbong.cafein.dataSet;

import com.dalbong.cafein.domain.subwayStation.SubwayStation;
import lombok.Data;

@Data
public class SubwayStationDto {

    private String statn_nm;

    private String route;

    private Double crdnt_x;

    private Double crdnt_y;

    private Integer statn_id;


    public SubwayStation toEntity(){

        return SubwayStation.builder()
                .stationName(this.statn_nm)
                .route(this.route)
                .lngX(this.crdnt_x)
                .latY(this.crdnt_y)
                .stationId(this.statn_id)
                .isUse(false)
                .build();
    }

}
