package com.dalbong.cafein.dto.store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

@Data
public class StoreSearchRequestDto {


    private String keyword;

    @Pattern(regexp = "^([-+]?\\d{1,2}([.]\\d+)?),\\s*([-+]?\\d{1,3}([.]\\d+)?)$",
            message = "잘못된 좌표 형식입니다.")
    private String coordinate;

    @Pattern(regexp = "^([-+]?\\d{1,2}([.]\\d+)?),([-+]?\\d{1,2}([.]\\d+)?)," +
            "\\s*([-+]?\\d{1,3}([.]\\d+)?),\\s*([-+]?\\d{1,3}([.]\\d+)?)$"
            , message = "잘못된 좌표 형식입니다.")
    private String rect;
}
