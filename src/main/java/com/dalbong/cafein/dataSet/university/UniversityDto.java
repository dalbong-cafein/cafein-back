package com.dalbong.cafein.dataSet.university;

import com.dalbong.cafein.domain.university.University;
import lombok.Data;

@Data
public class UniversityDto {

    private String name_kor;

    private String h_kor_city;

    private String h_kor_gu;

    private String h_kor_dong;

    private String add_kor;

    private String branch;

    private String type;

    private String cate1_name;

    private String main_key;

    private String hp;

    private String fax;

    private String tel;

    private String state;

    private String add_kor_road;

    private String postcode;

    private String year;

    public University toEntity(double lngX, double latY){

        return University.builder()
                .universityName(this.name_kor)
                .siNm(this.h_kor_city)
                .sggNm(this.h_kor_gu)
                .emdNm(this.h_kor_dong)
                .fullAddress(this.add_kor)
                .branch(this.branch)
                .type(this.type)
                .categoryName(this.cate1_name)
                .mainKey(this.main_key)
                .lngX(lngX)
                .latY(latY)
                .build();
    }
}
