package com.twentythree.peech.script.stt.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class RealTimeUtils {

    // ms를 시간 형식으로 변환
    public static LocalTime convertTimeStampToTimeFormat(int realTime) {
        int timestamp = realTime/1000;
        int hours = timestamp / 3600;
        int minutes = (timestamp % 3600) / 60;
        int seconds = timestamp % 60;
        int milliseconds = realTime % 1000;


        return LocalTime.of(hours, minutes, seconds, milliseconds * 1_000_000);
    }

    // 해당 문장을 발화하는데 걸리는 시간을 계산
    public static LocalTime getSentenceRealTime(int start, int end) {
        int realTime = (end - start); // ms to seconds
        return convertTimeStampToTimeFormat(realTime);
    }

    public static int getRealTimeStamp(int start, int end) {
        return end - start ;
    }
}
