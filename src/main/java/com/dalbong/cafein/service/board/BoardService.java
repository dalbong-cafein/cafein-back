package com.dalbong.cafein.service.board;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.dto.board.BoardResDto;

public interface BoardService {

    Board register(BoardResDto boardResDto, Long principalId);
}