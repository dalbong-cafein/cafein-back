package com.dalbong.cafein.controller.admin;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.admin.board.AdminBoardListResDto;
import com.dalbong.cafein.dto.admin.board.AdminBoardRegDto;
import com.dalbong.cafein.dto.admin.board.AdminBoardUpdateDto;
import com.dalbong.cafein.dto.page.PageRequestDto;
import com.dalbong.cafein.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class BoardAdminController {

    private final BoardService boardService;

    /**
     * 관리자단 게시글 등록
     */
    @PostMapping("/boards")
    public ResponseEntity<?> registerBoard(@Validated AdminBoardRegDto adminBoardRegDto, BindingResult bindingResult,
                                           @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {

        // boardService.register(adminBoardRegDto, principalDetails.getMember().getMemberId());
        boardService.register(adminBoardRegDto, 1L);

        return new ResponseEntity<>(new CMRespDto<>(1, "게시글 등록 성공", null), HttpStatus.CREATED);
    }

    /**
     * 관리자단 게시글 수정
     */
    @PutMapping("/boards/{boardId}")
    public ResponseEntity<?> modifyBoard(@Validated AdminBoardUpdateDto adminBoardUpdateDto, BindingResult bindingResult) throws IOException {

        boardService.modify(adminBoardUpdateDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "게시글 수정 성공", null), HttpStatus.OK);
    }

    /**
     * 관리자단 게시글 삭제
     */
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<?> removeBoard(@PathVariable("boardId") Long boardId){

        boardService.remove(boardId);

        return new ResponseEntity<>(new CMRespDto<>(1, "게시글 삭제 성공",null), HttpStatus.OK);
    }

    /**
     * 관리자단 게시글 리스트 조회
     */
    @GetMapping("/boards")
    public ResponseEntity<?> getBoardList(@RequestParam(value = "boardCategoryId", defaultValue = "1", required = false) Long boardCategoryId,
                                          PageRequestDto requestDto){

        AdminBoardListResDto adminBoardListResDto = boardService.getBoardListOfAdmin(boardCategoryId, requestDto);

        return new ResponseEntity<>(new CMRespDto<>(1, "관리자단 게시글 리스트 조회 성공", adminBoardListResDto), HttpStatus.OK);
    }

}
