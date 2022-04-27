package com.dalbong.cafein.domain.congestion;

public enum CongestionType {

    //Enum이 가질 열거형
    LESS(1),NORMAL(2), FULL(3);

    private int score;

    CongestionType(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }


}
