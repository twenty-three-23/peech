package com.twentythree.peech.script.dto;

public enum NowStatus {
    RealTime, ExpectedTime, RealAndExpectedTime;

    public static NowStatus parse(String status) {
        if (status.equals("RealTime")) {
            return RealTime;
        } else if (status.equals("ExpectedTime")) {
            return ExpectedTime;
        } else if (status.equals("RealAndExpectedTime")) {
            return RealAndExpectedTime;
        } else {
            throw new IllegalArgumentException("유효하지 않은 상태입니다.");
        }
    }
}
