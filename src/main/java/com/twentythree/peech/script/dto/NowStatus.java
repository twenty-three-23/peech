package com.twentythree.peech.script.dto;

public enum NowStatus {
    REALTIME, EXPECTEDTIME, REALANDEXPECTEDTIME;

    public static NowStatus parse(String status) {
        if (status.equals("RealTime")) {
            return REALTIME;
        } else if (status.equals("ExpectedTime")) {
            return EXPECTEDTIME;
        } else if (status.equals("RealAndExpectedTime")) {
            return REALANDEXPECTEDTIME;
        } else {
            throw new IllegalArgumentException("유효하지 않은 상태입니다.");
        }
    }
}
