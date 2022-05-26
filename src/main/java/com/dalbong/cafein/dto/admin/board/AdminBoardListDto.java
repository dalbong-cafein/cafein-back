package com.dalbong.cafein.dto.admin.board;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.dto.page.PageResultDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminBoardListDto {

    private long boardCnt;

    private PageResultDTO<AdminBoardResDto, Board> boardResDtoList;

}
