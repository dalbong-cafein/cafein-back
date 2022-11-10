package com.dalbong.cafein.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PossibleRegistrationResDto {

    private Boolean isPossibleRegistration;

    private String reason;

}
