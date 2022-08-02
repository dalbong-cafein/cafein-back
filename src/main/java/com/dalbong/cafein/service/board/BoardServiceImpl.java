package com.dalbong.cafein.service.board;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.board.BoardRepository;
import com.dalbong.cafein.domain.event.Event;
import com.dalbong.cafein.domain.event.EventRepository;
import com.dalbong.cafein.domain.image.BoardImage;
import com.dalbong.cafein.domain.image.BoardImageRepository;
import com.dalbong.cafein.domain.image.Image;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.domain.notice.BoardNoticeRepository;
import com.dalbong.cafein.dto.admin.board.AdminBoardListResDto;
import com.dalbong.cafein.dto.admin.board.AdminBoardRegDto;
import com.dalbong.cafein.dto.admin.board.AdminBoardResDto;
import com.dalbong.cafein.dto.admin.board.AdminBoardUpdateDto;
import com.dalbong.cafein.dto.board.BoardResDto;
import com.dalbong.cafein.dto.image.ImageDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.page.PageResultDTO;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.service.event.EventService;
import com.dalbong.cafein.service.image.ImageService;
import com.dalbong.cafein.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;
    private final ImageService imageService;
    private final NoticeService noticeService;
    private final MemberRepository memberRepository;
    private final BoardNoticeRepository boardNoticeRepository;
    private final EventService eventService;
    private final EventRepository eventRepository;

    /**
     * 게시글 등록
     */
    @Transactional
    @Override
    public Board register(AdminBoardRegDto adminBoardRegDto, Long principalId) throws IOException {

        //게시글 db 저장
        Board board = adminBoardRegDto.toEntity(principalId);
        boardRepository.save(board);

        //게시글 이미지 저장
        imageService.saveBoardImage(board, adminBoardRegDto.getImageFiles());

        //공지사항 알람 등록
        if(adminBoardRegDto.getBoardCategoryId().equals(1L)){
            List<Member> findMemberList = memberRepository.findAllNotLeave();

            noticeService.registerBoardNotice(board, findMemberList);
        }

        return board;
    }

    /**
     * 게시글 등록
     */
    @Transactional
    @Override
    public void modify(AdminBoardUpdateDto adminBoardUpdateDto) throws IOException {

        Board board = boardRepository.findById(adminBoardUpdateDto.getBoardId()).orElseThrow(() ->
                new CustomException("존재하지 않는 게시글입니다."));

        board.changeTitle(adminBoardUpdateDto.getTitle());
        board.changeContent(adminBoardUpdateDto.getContent());

        //기존 이미지 삭제
        if(adminBoardUpdateDto.getDeleteImageId() != null){
            imageService.remove(adminBoardUpdateDto.getDeleteImageId());
        }

        //이미지 추가
        if(adminBoardUpdateDto.getImageFile() != null && !adminBoardUpdateDto.getImageFile().isEmpty()){

            //새로운 이미지 갱신
            imageService.saveBoardImage(board, adminBoardUpdateDto.getImageFile());
        }
    }

    /**
     * 게시글 삭제
    */
    @Transactional
    @Override
    public void remove(Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(() ->
                new CustomException("존재하지 않는 게시글입니다."));

        //게시글 이미지 삭제
        List<BoardImage> boardImageList = board.getBoardImageList();

        for (BoardImage boardImage : boardImageList){
            imageService.remove(boardImage.getImageId());
        }

        //이벤트 삭제
        List<Event> eventList = eventRepository.findByBoard(board);

        if (eventList != null && !eventList.isEmpty()){
            for(Event event : eventList){
                eventService.remove(event.getEventId());
            }
        }

        //공지사항 알림 삭제
        boardNoticeRepository.deleteByBoard(board);

       //게시글 삭제
        boardRepository.deleteById(boardId);
    }

    /**
     * 게시글 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public List<BoardResDto> getBoardList(Long boardCategoryId) {

        List<Board> results = boardRepository.getBoardList(boardCategoryId);

        return results.stream().map(board -> {

            //게시글 이미지
            List<ImageDto> boardImageDtoList = new ArrayList<>();

            if (board.getBoardImageList() != null && !board.getBoardImageList().isEmpty()) {

                for (BoardImage boardImage : board.getBoardImageList()) {
                    boardImageDtoList.add(new ImageDto(boardImage.getImageId(), boardImage.getImageUrl()));
                }
            }
            return new BoardResDto(board, boardImageDtoList);
        }).collect(Collectors.toList());
    }

    /**
     * 관리자단 게시글 리스트 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminBoardListResDto getBoardListOfAdmin(Long boardCategoryId, PageRequestDto pageRequestDto) {

        Pageable pageable;

        if(pageRequestDto.getSort().equals("ASC")){
            pageable = pageRequestDto.getPageable(Sort.by("boardId").ascending());
        }else{
            pageable = pageRequestDto.getPageable(Sort.by("boardId").descending());
        }

        Page<Board> results = boardRepository.getBoardList(boardCategoryId, pageRequestDto.getKeyword(), pageable);

        Function<Board, AdminBoardResDto> fn = (board -> {
            //게시글 이미지
            List<ImageDto> boardImageDtoList = new ArrayList<>();

            if (board.getBoardImageList() != null && !board.getBoardImageList().isEmpty()) {

                for (BoardImage boardImage : board.getBoardImageList()) {
                    boardImageDtoList.add(new ImageDto(boardImage.getImageId(), boardImage.getImageUrl()));
                }
            }
            return new AdminBoardResDto(board, boardImageDtoList);
        });

        return new AdminBoardListResDto(results.getTotalElements(), new PageResultDTO<>(results, fn));
    }


}
