package com.dalbong.cafein.domain.review;

import com.dalbong.cafein.domain.BaseEntity;
import com.dalbong.cafein.domain.image.ReviewImage;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.store.Store;
import lombok.*;
import org.hibernate.annotations.BatchSize;

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
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(length = 1000)
    private String content = "";

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Recommendation recommendation;

    @Column(nullable = false)
    @Embedded
    private DetailEvaluation detailEvaluation;

    @Builder.Default
    @OneToMany(mappedBy = "review",fetch = FetchType.LAZY)
    private List<ReviewImage> reviewImageList = new ArrayList<>();

    //연관관계 메서드
    public void setStore(Store store){
        this.store = store;
        store.getReviewList().add(this);
    }

    public void changeContent(String content){
        this.content = content;
    }

    public void changeRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    public void changeDetailEvaluation(DetailEvaluation detailEvaluation){
        this.detailEvaluation = detailEvaluation;
    }
}
