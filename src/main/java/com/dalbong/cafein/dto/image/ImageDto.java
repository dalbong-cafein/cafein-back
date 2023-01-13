package com.dalbong.cafein.dto.image;

import com.dalbong.cafein.domain.image.Image;
import com.dalbong.cafein.domain.image.ReviewImage;
import com.dalbong.cafein.domain.image.StoreImage;
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

    public ImageDto(Long imageId, String imageUrl, String regNickname, boolean isCafein){
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.isCafein = isCafein;
        this.regNickname = regNickname;
    }

    public ImageDto(Long imageId, String imageUrl){
        this(imageId, imageUrl, "", false);
    }

    public ImageDto(Long imageId, String imageUrl, String regNickname){
        this(imageId, imageUrl, regNickname, false);
    }

    public ImageDto(Long imageId, String imageUrl, boolean isCafein){
        this(imageId, imageUrl, "", isCafein);
    }
}
