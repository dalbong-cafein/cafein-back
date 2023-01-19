package com.dalbong.cafein.dto.event;

import com.dalbong.cafein.domain.event.Event;
import com.dalbong.cafein.dto.image.ImageDto;
import lombok.Data;

@Data
public class EventResDto {

    private Long eventId;

    private Long boardId;

    private ImageDto eventImageDto;

    public EventResDto(Event event, ImageDto eventImageDto){

        this.eventId = event.getEventId();
        this.boardId = event.getBoard() != null ? event.getBoard().getBoardId() : null;
        this.eventImageDto = eventImageDto;

    }

}
