package com.dalbong.cafein.domain.subwayStation;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name="subwayStation_uk",
                        columnNames={"stationName", "route"}
                )
        }
)
@Entity
public class SubwayStation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subwayStationId;

    @Column(nullable = false)
    private String stationName;

    @Column(nullable = false)
    private String route;

    @Column(nullable = false)
    private Double lngX;

    @Column(nullable = false)
    private Double latY;

    private Integer stationId;

    @Builder.Default
    private Boolean isUse = false;




}
