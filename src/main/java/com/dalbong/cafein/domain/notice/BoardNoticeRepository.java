package com.dalbong.cafein.domain.notice;

import com.dalbong.cafein.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardNoticeRepository extends JpaRepository<BoardNotice,Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from BoardNotice bn where bn.board =:board")
    void deleteByBoard(@Param("board") Board board);


}
