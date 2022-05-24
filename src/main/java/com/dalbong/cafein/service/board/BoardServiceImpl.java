package com.dalbong.cafein.service.board;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.board.BoardRepository;
import com.dalbong.cafein.domain.image.BoardImageRepository;
import com.dalbong.cafein.dto.admin.board.AdminBoardRegDto;
import com.dalbong.cafein.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Transactional
@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
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


}
