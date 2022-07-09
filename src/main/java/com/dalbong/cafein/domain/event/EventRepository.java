package com.dalbong.cafein.domain.event;

import com.dalbong.cafein.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryQuerydsl {

    List<Event> findByBoard(Board board);

}
