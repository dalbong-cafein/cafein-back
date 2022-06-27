package com.dalbong.cafein.dto.review;

import com.dalbong.cafein.domain.review.DetailEvaluation;
import com.dalbong.cafein.domain.review.Recommendation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewUpdateDto {

    @NotNull
    private Long reviewId;

    @NotNull
    private Recommendation recommendation;

    @NotNull
    @Max(value = 4) @Min(value = 1)
    private int socket;

    @NotNull
    @Max(value = 4) @Min(value = 1)
    private int wifi;

    @NotNull
    @Max(value = 4) @Min(value = 1)
    private int restroom;

    @NotNull
    @Max(value = 4) @Min(value = 1)
    private int tableSize;

    @Length(max = 100)
    private String content;

    //이미지 추가
    private List<MultipartFile> updateImageFiles = new ArrayList<>();

    //이미지 삭제
    private List<Long> deleteImageIdList = new ArrayList<>();

    public DetailEvaluation getDetailEvaluation(){
        return new DetailEvaluation(this.socket, this.wifi, this.restroom, this.tableSize);
    }

}
