package com.dalbong.cafein.dto.event;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class EventRegDto {

    @NotNull
    private Long boardId;

    @NotNull
    private MultipartFile imageFile;
}
