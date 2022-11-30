package com.dalbong.cafein.dto.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class StoreSearchRequestDto {


    private String keyword;

    @Pattern(regexp = "^([-+]?\\d{1,2}([.]\\d+)?),\\s*([-+]?\\d{1,3}([.]\\d+)?)$",
    message = "좌표 포맷이 아닙니다.")
    private String coordinate;

    private String rect;


}
