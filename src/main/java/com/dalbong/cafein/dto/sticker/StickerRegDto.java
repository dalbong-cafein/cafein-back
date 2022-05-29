package com.dalbong.cafein.dto.sticker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StickerRegDto {

    private Long storeId;

    private Long reviewId;

    private Long congestionId;
}
