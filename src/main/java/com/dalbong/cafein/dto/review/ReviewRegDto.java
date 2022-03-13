package com.dalbong.cafein.dto.review;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.DetailEvaluation;
import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.store.Feature;
import com.dalbong.cafein.domain.store.SocketCnt;
import com.dalbong.cafein.domain.store.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewRegDto {

    private Long storeId;

    private Recommendation recommendation;

    private int socket;

    private int wifi;

    private int restroom;

    private int tableSize;

    @Length(max = 100)
    private String content;

    private List<MultipartFile> imageFiles;

    //TODO member 엔티티와 양방향 연관관계
    public Review toEntity(Long principalId, Store store){

        Review review = Review.builder()
                .member(Member.builder().memberId(principalId).build())
                .recommendation(recommendation)
                .detailEvaluation(new DetailEvaluation(socket, wifi, restroom, tableSize))
                .content(content)
                .build();

        review.setStore(store);

        return review;
    }

}
