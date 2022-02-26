package com.dalbong.cafein.dto.board;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.boardCategory.BoardCategory;
import com.dalbong.cafein.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardResDto {

    private String title;

    private String content;

    private Long categoryId;

    public Board toEntity(Long principalId){
        return Board.builder()
                .member(Member.builder().memberId(principalId).build())
                .boardCategory(BoardCategory.builder().categoryId(categoryId).build())
                .title(title)
                .content(content)
                .build();
    }
}
