package com.dalbong.cafein.dto.admin.event;

import com.dalbong.cafein.domain.event.Event;
import com.dalbong.cafein.domain.image.Image;
import com.dalbong.cafein.dto.image.ImageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminEventResDto {

    private Long eventId;

    private Long boardId;

    private ImageDto eventImageDto;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    public AdminEventResDto(Event event, ImageDto eventImageDto){

        this.eventId = event.getEventId();
        this.boardId = event.getBoard().getBoardId();
        this.eventImageDto = eventImageDto;
        this.regDateTime = event.getRegDateTime();
    }
}
