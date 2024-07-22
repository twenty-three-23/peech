package com.twentythree.peech.script.dto;

public enum NowStatus {
    REALTIME, EXPECTEDTIME, REALANDEXPECTEDTIME;

    public static NowStatus parse(String status) {
        if (status.equals("REALTIME")) {
            return REALTIME;
        } else if (status.equals("EXPECTEDTIME")) {
            return EXPECTEDTIME;
        } else if (status.equals("REALANDEXPECTEDTIME")) {
            return REALANDEXPECTEDTIME;
        } else {
            throw new IllegalArgumentException("유효하지 않은 상태입니다.");
        }
    }
}
