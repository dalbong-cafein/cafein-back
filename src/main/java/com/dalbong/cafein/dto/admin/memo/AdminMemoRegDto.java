package com.dalbong.cafein.dto.admin.memo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminMemoRegDto {

    private Long storeId;

    private Long reviewId;

    private Long memberId;

    private String content = "";

}
