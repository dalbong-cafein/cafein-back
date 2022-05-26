package com.dalbong.cafein.service.board;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.dto.admin.board.AdminBoardListDto;
import com.dalbong.cafein.dto.admin.board.AdminBoardRegDto;
import com.dalbong.cafein.dto.admin.board.AdminBoardResDto;
import com.dalbong.cafein.dto.page.PageRequestDto;

import java.io.IOException;
import java.util.List;

public interface BoardService {

    Board register(AdminBoardRegDto adminBoardRegDto, Long principalId) throws IOException;

    void remove(Long boardId);

    AdminBoardListDto getBoardListOfAdmin(Long boardCategoryId, PageRequestDto pageRequestDto);

}
