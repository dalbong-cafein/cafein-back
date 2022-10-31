package com.dalbong.cafein.controller;

import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.board.BoardResDto;
import com.dalbong.cafein.service.board.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    /**
     * 카테고리별 게시글 리스트 조회
     */
    @GetMapping("/boards")
    public ResponseEntity<?> getBoardList(@RequestParam(value = "boardCategoryId") Long boardCategoryId){

        List<BoardResDto> boardResDtoList = boardService.getBoardList(boardCategoryId);

        return new ResponseEntity<>(new CMRespDto<>(1, "게시글 리스트 조회 성공", boardResDtoList), HttpStatus.OK);
    }

    /**
     * 게시를 상세 조회
     */
    @GetMapping("/boards/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable("boardId") Long boardId){

        BoardResDto boardResDto = boardService.getBoard(boardId);

        return new ResponseEntity<>(new CMRespDto<>(1, "게시글 상세 조회 성공", boardResDto), HttpStatus.OK);
    }





}
