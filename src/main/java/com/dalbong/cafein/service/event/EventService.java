package com.dalbong.cafein.service.event;

import com.dalbong.cafein.domain.event.Event;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface EventService {

    Event register(MultipartFile imageFile, Long boardId) throws IOException;

}
