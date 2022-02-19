package com.dalbong.cafein.dto.review;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.store.Feature;
import com.dalbong.cafein.domain.store.SocketCnt;
import com.dalbong.cafein.domain.store.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewRegDto {

    private Long storeId;

    private int grade;

    private String content;

    private SocketCnt socketCnt;

    private List<Feature> featureList = new ArrayList<>();

    //TODO 이미지

    //TODO member 엔티티와 양방향 연관관계
    public Review toEntity(Long principalId, Store store){
        Review review = Review.builder()
                .member(Member.builder().memberId(principalId).build())
                .grade(grade)
                .content(content)
                .socketCnt(socketCnt)
                .featureList(featureList)
                .build();

        review.setStore(store);

        return review;
    }

}
