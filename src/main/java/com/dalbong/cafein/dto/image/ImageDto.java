package com.dalbong.cafein.dto.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ImageDto {

    private Long imageId;

    private Boolean isGoogle;

    private String imageUrl;

    public ImageDto(Long imageId, String imageUrl){
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.isGoogle = false;
    }

    public ImageDto(Long imageId, String imageUrl, boolean isGoogle){
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.isGoogle = isGoogle;
    }




}
