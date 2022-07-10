package com.dalbong.cafein.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PhoneUpdateDto {

    @NotBlank
    private String phone;
}
