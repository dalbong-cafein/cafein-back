package com.dalbong.cafein.domain.congestion;

public enum CongestionType {

    //Enum이 가질 열거형
    LESS(1, "LESS"),NORMAL(2, "NORMAL"), FULL(3, "FULL");


    private int index;
    private String type;

    CongestionType(int index, String subjectName) {
        this.type = type;
    }

    //getStudentIndex의 Getter
    public int getIndex() {
        return this.index;
    }

    //getStudentName의 Getter
    public String getType() {
        return this.type;
    }
}
