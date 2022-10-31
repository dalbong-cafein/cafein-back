package com.dalbong.cafein.service.board;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.dto.admin.board.AdminBoardListResDto;
import com.dalbong.cafein.dto.admin.board.AdminBoardRegDto;
import com.dalbong.cafein.dto.admin.board.AdminBoardUpdateDto;
import com.dalbong.cafein.dto.board.BoardResDto;
import com.dalbong.cafein.dto.page.PageRequestDto;

import java.io.IOException;
import java.util.List;

public interface BoardService {

    Board register(AdminBoardRegDto adminBoardRegDto, Long principalId) throws IOException;

    void modify(AdminBoardUpdateDto adminBoardUpdateDto) throws IOException;

    void remove(Long boardId);

    List<BoardResDto> getBoardList(Long boardCategoryId);

    BoardResDto getBoard(Long boardId);

    AdminBoardListResDto getBoardListOfAdmin(Long boardCategoryId, PageRequestDto pageRequestDto);

}
