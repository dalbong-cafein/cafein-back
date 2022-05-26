package com.dalbong.cafein.domain.board;

import com.dalbong.cafein.domain.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryQuerydsl {

    /**
     * 관리자단 게시글 리스트 조회 - 공지사항, 자주묻는 질문
     */
    Page<Board> getBoardList(Long boardCategoryId, String keyword, Pageable pageable);
}
