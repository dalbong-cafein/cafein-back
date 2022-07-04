package com.dalbong.cafein.dto.admin.eventImage;

import com.dalbong.cafein.domain.image.Image;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminEventImageResDto {

    private Long eventImageId;

    private String eventImageUrl;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    public AdminEventImageResDto(Image eventImage){

        this.eventImageId = eventImage.getImageId();
        this.eventImageUrl = eventImage.getImageUrl();
        this.regDateTime = eventImage.getRegDateTime();
    }
}
