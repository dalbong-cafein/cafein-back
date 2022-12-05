package com.dalbong.cafein.domain.university;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
public class University {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long universityId;

    @Column(nullable = false)
    private String universityName;

    private String branch;

    @Column(nullable = false)
    private String siNm;

    @Column(nullable = false)
    private String sggNm;

    @Column(nullable = false)
    private String emdNm;

    @Column(nullable = false)
    private String fullAddress;

    private String type;

    private String categoryName;

    @Column(nullable = false)
    private Double lngX;

    @Column(nullable = false)
    private Double latY;

    @Column(unique = true)
    private String mainKey;
}
