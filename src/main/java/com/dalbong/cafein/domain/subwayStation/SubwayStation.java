package com.dalbong.cafein.domain.subwayStation;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@Getter
@ToString
@Entity
public class SubwayStation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subwayStationId;

    @Column(nullable = false, unique = true)
    private String stationName;

    @Column(nullable = false)
    private String route;

    @Column(nullable = false)
    private Double lngX;

    @Column(nullable = false)
    private Double latY;






}
