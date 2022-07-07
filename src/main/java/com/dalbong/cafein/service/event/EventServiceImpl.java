package com.dalbong.cafein.service.event;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.board.BoardRepository;
import com.dalbong.cafein.domain.event.Event;
import com.dalbong.cafein.domain.event.EventRepository;
import com.dalbong.cafein.domain.image.Image;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Transactional
@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;
    private final ImageService imageService;
    private final BoardRepository boardRepository;

    /**
     * 이벤트 등록
     */
    @Transactional
    @Override
    public Event register(MultipartFile imageFile, Long boardId) throws IOException {

        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new CustomException("존재하지 않는 게시글입니다."));


        Event event = Event.builder().board(board).build();

        eventRepository.save(event);

        //이벤트 이미지 저장
        imageService.saveEventImage(event, imageFile);

        return event;
    }
}
