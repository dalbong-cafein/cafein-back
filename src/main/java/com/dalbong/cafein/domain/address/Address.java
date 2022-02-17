package com.dalbong.cafein.domain.address;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable //jpa 내장타입
@Getter
public class Address {

    private String siNm; //시도명
    private String sggNm; //시군구
    private String rn; //도로명


    protected Address() {
    }

    public Address(String siNm, String sggNm, String rn) {
        this.siNm = siNm;
        this.sggNm = sggNm;
        this.rn = rn;
    }
}
