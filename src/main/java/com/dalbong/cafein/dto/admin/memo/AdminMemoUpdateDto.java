package com.dalbong.cafein.dto.admin.memo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminMemoUpdateDto {

    @NotNull
    private Long memoId;

    private String content = "";
}
