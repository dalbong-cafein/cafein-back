package com.dalbong.cafein.domain.board;

import com.dalbong.cafein.domain.BaseEntity;
import com.dalbong.cafein.domain.boardCategory.BoardCategory;
import com.dalbong.cafein.domain.image.BoardImage;
import com.dalbong.cafein.domain.image.ReviewImage;
import com.dalbong.cafein.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member","boardCategory"})
@Entity
public class Board extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_category_id", nullable = false)
    private BoardCategory boardCategory;

    @Column(nullable = false)
    private String title;

    @Builder.Default
    @Column(nullable = false, length = 2000)
    private String content = "";

    @Builder.Default
    @OneToMany(mappedBy = "board",fetch = FetchType.LAZY)
    private List<BoardImage> boardImageList = new ArrayList<>();

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content = content;
    }

}
