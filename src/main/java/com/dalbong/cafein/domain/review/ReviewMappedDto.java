package com.dalbong.cafein.domain.review;

import com.dalbong.cafein.domain.image.MemberImage;
import com.dalbong.cafein.domain.store.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ReviewMappedDto {

    private Review review;

    private Store store;

}
