package com.dalbong.cafein.service.event;

import com.dalbong.cafein.domain.event.Event;
import com.dalbong.cafein.dto.admin.event.AdminEventListResDto;
import com.dalbong.cafein.dto.event.EventResDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EventService {

    Event register(MultipartFile imageFile, Long boardId) throws IOException;

    AdminEventListResDto<?> getEventListOfAdmin(PageRequestDto pageRequestDto);

}
