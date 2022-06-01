package com.dalbong.cafein.domain.reportCategory;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
public class ReportCategory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportCategoryId;

    private String categoryName;
}
