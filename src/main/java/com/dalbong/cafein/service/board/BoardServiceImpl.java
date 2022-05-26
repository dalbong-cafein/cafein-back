package com.dalbong.cafein.service.board;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.board.BoardRepository;
import com.dalbong.cafein.domain.image.BoardImage;
import com.dalbong.cafein.domain.image.BoardImageRepository;
import com.dalbong.cafein.domain.image.ReviewImage;
import com.dalbong.cafein.domain.review.Review;
import com.dalbong.cafein.dto.admin.board.AdminBoardListDto;
import com.dalbong.cafein.dto.admin.board.AdminBoardRegDto;
import com.dalbong.cafein.dto.admin.board.AdminBoardResDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewListDto;
import com.dalbong.cafein.dto.admin.review.AdminReviewResDto;
import com.dalbong.cafein.dto.board.BoardListResDto;
import com.dalbong.cafein.dto.board.BoardResDto;
import com.dalbong.cafein.dto.image.ImageDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.dto.page.PageResultDTO;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.service.image.ImageService;
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

        return board;
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
    public AdminBoardListDto getBoardListOfAdmin(Long boardCategoryId, PageRequestDto pageRequestDto) {

        Pageable pageable;

        if(pageRequestDto.getSort().equals("ASC")){
            pageable = pageRequestDto.getPageable(Sort.by("boardId").ascending());
        }else{
            pageable = pageRequestDto.getPageable(Sort.by("boardId").descending());
        }

        Page<Board> results = boardRepository.getBoardList(boardCategoryId, pageRequestDto.getKeyword(), pageable);

        Function<Board, AdminBoardResDto> fn = (AdminBoardResDto::new);

        return new AdminBoardListDto(results.getTotalElements(), new PageResultDTO<>(results, fn));
    }


}
