package com.dalbong.cafein.domain.image;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.review.Review;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString(exclude = {"board"})
@DiscriminatorValue("board")
@Entity
public class BoardImage extends Image{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    protected BoardImage(){}

    public BoardImage(Board board, String imageUrl){
        super(imageUrl);
        this.board = board;
        board.getBoardImageList().add(this);
    }

}
