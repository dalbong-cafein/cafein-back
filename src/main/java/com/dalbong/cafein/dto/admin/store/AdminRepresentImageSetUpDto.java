package com.dalbong.cafein.dto.admin.store;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AdminRepresentImageSetUpDto {

    @NotNull
    private Long storeId;

    @NotNull
    private Long representImageId;

}
