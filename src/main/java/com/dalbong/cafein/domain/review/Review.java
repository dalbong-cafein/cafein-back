package com.dalbong.cafein.domain.review;

import com.dalbong.cafein.domain.BaseEntity;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.store.Store;
import lombok.*;

import javax.persistence.*;


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

    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Recommendation recommendation;

    @Embedded
    private DetailEvaluation detailEvaluation;

    //연관관계 메서드
    public void setStore(Store store){
        this.store = store;
        store.getReviewList().add(this);
    }

}
