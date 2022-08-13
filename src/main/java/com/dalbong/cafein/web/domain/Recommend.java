package com.dalbong.cafein.web.domain;

import com.dalbong.cafein.domain.BaseEntity;
import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.domain.store.Store;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"store"})
@Entity
public class Recommend extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private String sessionId;

    @Enumerated(EnumType.STRING)
    private Recommendation recommendation;

    //연관관계 메서드
    public void setStore(Store store){
        this.store = store;
        store.getRecommendList().add(this);
    }
}
