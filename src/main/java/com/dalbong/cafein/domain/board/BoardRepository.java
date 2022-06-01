package com.dalbong.cafein.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long>, BoardRepositoryQuerydsl {

    /**
     * 카테고리별 게시글 리스트 조회
     */
    @Query("select b from Board b where b.boardCategory.boardCategoryId =:boardCategoryId " +
            "order by b.boardId desc")
    List<Board> getBoardList(@Param("boardCategoryId") Long boardCategoryId);

}
