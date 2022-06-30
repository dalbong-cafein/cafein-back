package com.dalbong.cafein.dataSet;

import lombok.Data;

import java.util.Map;

@Data
public class NaverCloudDto {

    public NaverCloudDto(Double x, Double y, String roadAddress){
       this.x = x;
       this.y = y;
       this.roadAddress = roadAddress;
    }

    private double x;
    private double y;
    private String roadAddress;
}
