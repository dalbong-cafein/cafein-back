package com.dalbong.cafein.dto.admin.review;

import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.dto.page.PageResultDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminReviewListResDto {

    private long reviewCnt;

    private PageResultDTO<AdminReviewResDto, Object[]> reviewResDtoList;

}
