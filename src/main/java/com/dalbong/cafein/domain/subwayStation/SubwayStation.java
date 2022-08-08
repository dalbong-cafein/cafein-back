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
                        columnNames={"station_name", "route"}
                )
        }
)
@Entity
public class SubwayStation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subwayStationId;

    @Column(nullable = false, name = "station_name")
    private String stationName;

    @Column(nullable = false)
    private String route;

    @Column(nullable = false)
    private Double lngX;

    @Column(nullable = false)
    private Double latY;

    @Column(unique = true)
    private Integer stationId;

    private String sggNm;

    @Builder.Default
    private Boolean isUse = false;

    public void use(){
        this.isUse = true;
    }

    public void changeSggNm(String sgg){
        this.sggNm = sgg;
    }

}
