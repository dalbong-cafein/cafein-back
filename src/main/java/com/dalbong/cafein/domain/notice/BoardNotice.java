package com.dalbong.cafein.domain.notice;

import com.dalbong.cafein.domain.board.Board;
import com.dalbong.cafein.domain.coupon.Coupon;
import com.dalbong.cafein.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@ToString(exclude = {"board"})
@DiscriminatorValue("board")
@Entity
public class BoardNotice extends Notice{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public BoardNotice(Board board, Member toMember){
        super(toMember, board.getTitle());
        this.board = board;
    }

    public BoardNotice(Board board, Member toMember, String content){
        super(toMember, content);
        this.board = board;
    }
}
