package com.dalbong.cafein.domain.boardCategory;

import lombok.*;

import javax.persistence.*;

/**
 * 게시판 유형 카테고리입니다
 * ex) 공지사항, 자주 묻는 질문
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
public class BoardCategory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardCategoryId;

    @Column(unique = true, nullable = false)
    private String categoryName;
    
}
