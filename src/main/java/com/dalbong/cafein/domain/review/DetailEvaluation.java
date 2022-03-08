package com.dalbong.cafein.domain.review;
import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable //jpa 내장타입
@Getter
public class DetailEvaluation {

    private int socket;

    private int wifi;

    private int restroom;

    private int tableSize;

    protected DetailEvaluation(){}

    public DetailEvaluation(int socket, int wifi, int restroom, int tableSize){
        this.socket = socket;
        this.wifi = wifi;
        this.restroom = restroom;
        this.tableSize = tableSize;
    }
}
