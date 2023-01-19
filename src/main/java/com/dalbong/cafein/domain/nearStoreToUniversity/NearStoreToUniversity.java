package com.dalbong.cafein.domain.nearStoreToUniversity;

import com.dalbong.cafein.domain.store.Store;
import com.dalbong.cafein.domain.subwayStation.SubwayStation;
import com.dalbong.cafein.domain.university.University;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude = {"store","university"})
@Getter
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name="near_uk",
                        columnNames={"store_id", "university_id"}
                )
        }
)
@Entity
public class NearStoreToUniversity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nearId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    @Column(nullable = false)
    private Double distance;

    //연관관계 메서드
    public void setStore(Store store){
        this.store = store;
        store.getNearStoreToUniversityList().add(this);
    }
}
