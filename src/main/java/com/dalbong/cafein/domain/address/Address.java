package com.dalbong.cafein.domain.address;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Embeddable;

@ToString
@Embeddable //jpa 내장타입
@Getter
public class Address {

    private String siNm; //시도명
    private String sggNm; //시군구
    private String rNm; //도로명
    private String rNum; //도로 number
    private String detail; //상세주소
    private String fullAddress;

    protected Address() {
    }

    public Address(String siNm, String sggNm, String rNm, String rNum, String detail) {
        this.siNm = siNm;
        this.sggNm = sggNm;
        this.rNm = rNm;
        this.rNum = rNum;
        this.detail = detail;
        if(detail == null){
            this.fullAddress = siNm + " " + sggNm + " " + rNm + " " + rNum;
        }else{
            this.fullAddress = siNm + " " + sggNm + " " + rNm + " " + rNum + " " + detail;
        }

    }

}
