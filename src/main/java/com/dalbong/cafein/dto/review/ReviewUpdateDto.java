package com.dalbong.cafein.dto.review;

import com.dalbong.cafein.domain.review.DetailEvaluation;
import com.dalbong.cafein.domain.review.Recommendation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewUpdateDto {

    private Long reviewId;

    private Recommendation recommendation;

    private int socket;

    private int wifi;

    private int restroom;

    private int tableSize;

    @Length(max = 100)
    private String content;

    private List<MultipartFile> imageFiles;

    public DetailEvaluation getDetailEvaluation(){
        return new DetailEvaluation(this.socket, this.wifi, this.restroom, this.tableSize);
    }

}
