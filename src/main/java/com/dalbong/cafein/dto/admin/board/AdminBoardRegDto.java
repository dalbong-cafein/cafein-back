package com.dalbong.cafein.dto.admin.board;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.boardCategory.BoardCategory;
import com.dalbong.cafein.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminBoardRegDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content = "";

    @NotNull
    private Long boardCategoryId;

    private List<MultipartFile> imageFiles = new ArrayList<>();

    public Board toEntity(Long principalId){
        return Board.builder()
                .member(Member.builder().memberId(principalId).build())
                .boardCategory(BoardCategory.builder().boardCategoryId(this.boardCategoryId).build())
                .title(title)
                .content(content)
                .build();
    }
}
