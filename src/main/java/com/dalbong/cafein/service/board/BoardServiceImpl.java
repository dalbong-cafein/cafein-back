package com.dalbong.cafein.service.board;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.board.BoardRepository;
import com.dalbong.cafein.dto.board.BoardResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;

    /**
     * 게시글 등록
     */
    @Transactional
    @Override
    public Board register(BoardResDto boardResDto, Long principalId) {

        Board board = boardResDto.toEntity(principalId);

        return boardRepository.save(board);
    }


}
