package com.dalbong.cafein.dataSet;

import lombok.Data;

import java.util.Map;

@Data
public class NaverCloudDto {

    public NaverCloudDto(Double x, Double y){
       this.x = x;
       this.y = y;
    }

    private double x;
    private double y;
}
