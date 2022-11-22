package com.dalbong.cafein.dto.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageDto {

    private Long imageId;

    private Boolean isCafein;

    private String imageUrl;

    private String regNickname;

    public ImageDto(Long imageId, String imageUrl){
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.isCafein = false;
        this.regNickname = "";
    }

    public ImageDto(Long imageId, String imageUrl, String regNickname){
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.isCafein = false;
        this.regNickname = regNickname;
    }

    public ImageDto(Long imageId, String imageUrl, String regNickname, boolean isCafein){
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.isCafein = isCafein;
        this.regNickname = regNickname;
    }




}
