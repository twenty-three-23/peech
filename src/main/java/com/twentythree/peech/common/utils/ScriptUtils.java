package com.twentythree.peech.common.utils;

import java.time.LocalTime;

public class ScriptUtils {
    public static LocalTime calculateExpectedTime(String text) {

        final float DEFAULT_TIME_PER_WORD_SECOND = 1.75F; //LocalTime.of(0,0,1, 750000000);

        String[] words = text.split(" ");
        int wordsCount = words.length;

        float expectedTimeToSecond = wordsCount * DEFAULT_TIME_PER_WORD_SECOND;

        LocalTime expectedTime = transferSeoondToLocalTime(expectedTimeToSecond);

        return expectedTime;
    }

    public static LocalTime transferSeoondToLocalTime(float time) {

        int second = (int) time;

        int minute = (int) (second / 60);
        int hour = (int) (second / (60 * 60));

        int minuteSet = minute - (hour * 60);
        int secondSet = second - (minuteSet * 60);

        return LocalTime.of(hour, minuteSet, secondSet);
    }

    public static LocalTime sumLocalTime(LocalTime time1, LocalTime time2) {
        LocalTime expectedTime = time1.plusNanos(time2.getNano())
                .plusSeconds(time2.getSecond())
                .plusMinutes(time2.getMinute())
                .plusHours(time2.getHour());

        return expectedTime;
    }

}
