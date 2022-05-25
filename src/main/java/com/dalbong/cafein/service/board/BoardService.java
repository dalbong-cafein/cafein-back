package com.dalbong.cafein.service.board;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.dto.admin.board.AdminBoardRegDto;

import java.io.IOException;

public interface BoardService {

    Board register(AdminBoardRegDto adminBoardRegDto, Long principalId) throws IOException;

    void remove(Long boardId);
}
