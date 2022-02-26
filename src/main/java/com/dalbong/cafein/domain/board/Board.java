package com.dalbong.cafein.domain.board;

import com.dalbong.cafein.domain.BaseEntity;
import com.dalbong.cafein.domain.boardCategory.BoardCategory;
import com.dalbong.cafein.domain.member.Member;
import lombok.*;

import javax.persistence.*;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private BoardCategory boardCategory;

    @Column(nullable = false)
    private String title;

    @Builder.Default
    @Column(nullable = false)
    private String content = "";

}
