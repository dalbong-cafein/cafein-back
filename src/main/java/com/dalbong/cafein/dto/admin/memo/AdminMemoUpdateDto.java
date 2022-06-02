package com.dalbong.cafein.dto.admin.memo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminMemoUpdateDto {

    private Long memoId;

    private String content;
}
