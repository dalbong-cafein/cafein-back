package com.dalbong.cafein.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardListResDto<T> {

    private long boardCnt;

    private T dtoList;
}
