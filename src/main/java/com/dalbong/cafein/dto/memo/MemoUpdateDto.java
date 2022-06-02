package com.dalbong.cafein.dto.memo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemoUpdateDto {

    private Long memoId;

    private String content;
}
