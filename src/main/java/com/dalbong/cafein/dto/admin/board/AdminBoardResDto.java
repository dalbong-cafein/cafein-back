package com.dalbong.cafein.dto.admin.board;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.dto.image.ImageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminBoardResDto {

    private Long boardId;

    private String title;

    private String content;

    private List<ImageDto> boardImageDtoList = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    public AdminBoardResDto(Board board, List<ImageDto> boardImageDtoList){
        this.boardId = board.getBoardId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.regDateTime = board.getRegDateTime();
        this.boardImageDtoList = boardImageDtoList;
    }

}
