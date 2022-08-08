package com.dalbong.cafein.domain.nearStoreToSubwayStation;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.subwayStation.SubwayStation;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"store","subwayStation"})
@Getter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name="near_uk",
                        columnNames={"store_id", "subway_station_id"}
                )
        }
)
@Entity
public class NearStoreToSubwayStation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nearId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subway_station_id", nullable = false)
    private SubwayStation subwayStation;

    @Column(nullable = false)
    private Double distance;



}
