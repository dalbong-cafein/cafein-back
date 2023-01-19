package com.dalbong.cafein.dto.admin.review;

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
public class AdminReviewUpdateDto {

    @NotNull
    private Long reviewId;

    //이미지 추가
    private List<MultipartFile> updateImageFiles = new ArrayList<>();

    //이미지 삭제
    private List<Long> deleteImageIdList = new ArrayList<>();

}
