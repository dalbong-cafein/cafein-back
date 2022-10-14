package com.dalbong.cafein.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AgreeRegDto {

    @NotNull
    private Boolean isAgreeLocation;

    @NotNull
    private Boolean isAgreeMarketingPush;


}
