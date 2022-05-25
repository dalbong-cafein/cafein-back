package com.dalbong.cafein.dto.review;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.review.DetailEvaluation;
import com.dalbong.cafein.domain.review.Recommendation;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.domain.store.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewRegDto {

    private Long storeId;

    private Recommendation recommendation;

    @Max(value = 4) @Min(value = 1)
    private int socket;

    @Max(value = 4) @Min(value = 1)
    private int wifi;

    @Max(value = 4) @Min(value = 1)
    private int restroom;

    @Max(value = 4) @Min(value = 1)
    private int tableSize;

    @Length(max = 100)
    private String content;

    @Builder.Default
    private List<MultipartFile> imageFiles = new ArrayList<>();

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
