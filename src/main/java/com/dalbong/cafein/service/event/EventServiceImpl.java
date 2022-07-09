package com.dalbong.cafein.service.event;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.board.BoardRepository;
import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.domain.event.Event;
import com.dalbong.cafein.domain.event.EventRepository;
import com.dalbong.cafein.domain.image.EventImage;
import com.dalbong.cafein.domain.image.EventImageRepository;
import com.dalbong.cafein.domain.image.Image;
import com.dalbong.cafein.dto.admin.coupon.AdminCouponResDto;
import com.dalbong.cafein.dto.admin.event.AdminEventListResDto;
import com.dalbong.cafein.dto.admin.event.AdminEventResDto;
import com.dalbong.cafein.dto.event.EventResDto;
import com.dalbong.cafein.dto.image.ImageDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.page.PageResultDTO;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.service.image.ImageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

@Transactional
@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;
    private final ImageService imageService;
    private final EventImageRepository eventImageRepository;
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

    /**
     * 이벤트 삭제
     */
    @Transactional
    @Override
    public void remove(Long eventId) {

        //이벤트 이미지 삭제
        EventImage eventImage = eventImageRepository.findByEvent_EventId(eventId).orElseThrow(() ->
                new CustomException("존재하지 않는 이벤트 이미지입니다."));
        imageService.remove(eventImage.getImageId());

        //이벤트 삭제
        eventRepository.deleteById(eventId);
    }

    /**
     * 가장 최신의 이벤트 배너 조회
     */
    @Transactional(readOnly = true)
    @Override
    public EventResDto getLatestEvent() {

        Object[] arr = eventRepository.latestEvent().orElseThrow(() ->
                new CustomException("이벤트 배너가 존재하지 않습니다."));

        Event event = (Event) arr[0];
        EventImage eventImage = (EventImage) arr[1];

        //이벤트 배너 이미지
        ImageDto eventImageDto = null;
        if(eventImage != null){
            eventImageDto = new ImageDto(eventImage.getImageId(), eventImage.getImageUrl());
        }

        return new EventResDto(event, eventImageDto);
    }

    /**
     * 관리자단 이벤트 배너 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminEventListResDto<?> getEventListOfAdmin(PageRequestDto pageRequestDto) {

        Pageable pageable;

        if(pageRequestDto.getSort().equals("ASC")){
            pageable = pageRequestDto.getPageable(Sort.by("eventId").ascending());
        }else{
            pageable = pageRequestDto.getPageable(Sort.by("eventId").descending());
        }

        Page<Object[]> results = eventRepository.getEventList(pageable);

        Function<Object[], AdminEventResDto> fn = (arr-> {

            Event event = (Event) arr[0];
            EventImage eventImage = (EventImage) arr[1];

            ImageDto eventImageDto = null;
            if(eventImage != null){
                eventImageDto = new ImageDto(eventImage.getImageId(), eventImage.getImageUrl());
            }

            return new AdminEventResDto(event, eventImageDto);
        });

        return new AdminEventListResDto<>(results.getTotalElements(), new PageResultDTO<>(results, fn));
    }

    /**
     * 이벤트 삭제
     */
}
