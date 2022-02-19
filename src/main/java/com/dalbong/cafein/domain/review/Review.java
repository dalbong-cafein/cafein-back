package com.dalbong.cafein.domain.review;

import com.dalbong.cafein.domain.BaseEntity;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.store.Feature;
import com.dalbong.cafein.domain.store.SocketCnt;
import com.dalbong.cafein.domain.store.Store;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member","store"})
@Entity
public class Review extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private int grade;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocketCnt socketCnt;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "feature_review",joinColumns = @JoinColumn(name = "review_id"))
    @Builder.Default
    @Column(name = "feature")
    private List<Feature> featureList = new ArrayList<>();

    //TODO 이미지

    //연관관계 메서드
    public void setStore(Store store){
        this.store = store;
        store.getReviewList().add(this);
    }

}
