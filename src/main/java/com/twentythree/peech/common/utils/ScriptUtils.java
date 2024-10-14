package com.twentythree.peech.common.utils;

import java.time.LocalTime;
import java.util.List;

public class ScriptUtils {
    public static LocalTime calculateExpectedTime(String text) {
        final int DEFAULT_SPM = 355;

        long count = text.chars().filter(ch -> ch == '.').count();

        String noSpaceText = text.replaceAll("[^가-힣]", "");
        int SyllableFromText = noSpaceText.length();
        float expectedTimeToSecond = (float) SyllableFromText / DEFAULT_SPM * 60 + count * 1;

        LocalTime expectedTime = transferSeoondToLocalTime(expectedTimeToSecond);

        return expectedTime;
    }

    public static LocalTime calculateParagraphTime(List<String> paragraph) {
        LocalTime paragraphExpectedTime = LocalTime.of(0, 0, 0, 0);

        for(String sentence : paragraph) {
            LocalTime sentenceExpectedTime = calculateExpectedTime(sentence);
            paragraphExpectedTime = sumLocalTime(paragraphExpectedTime, sentenceExpectedTime);
        }

        return paragraphExpectedTime;
    }

    public static LocalTime transferSeoondToLocalTime(float time) {

        int second = (int) time;

        int hour = second / 3600;
        int minute = (second % 3600) / 60;
        int secondSet = (second % 3600) % 60;
        int milliSecond = ((int)((time-second)*100)) * 10_000_000;

        return LocalTime.of(hour, minute, secondSet, milliSecond);
    }

    public static LocalTime sumLocalTime(LocalTime time1, LocalTime time2) {
        LocalTime expectedTime = time1.plusNanos(time2.getNano())
                .plusSeconds(time2.getSecond())
                .plusMinutes(time2.getMinute())
                .plusHours(time2.getHour());

        return expectedTime;
    }

    public static String measurementSpeedResult(LocalTime realTimePerParagraph, LocalTime expectedTimePerParagraph) {

        int realTimePerSecond = realTimePerParagraph.toSecondOfDay();
        int expectedTimePerSecond = expectedTimePerParagraph.toSecondOfDay();

        double bias = expectedTimePerSecond * 0.1;

        int lowerBound = (int) Math.round(expectedTimePerSecond - bias);
        int upperBound = (int) Math.round(expectedTimePerSecond + bias);

        if (realTimePerSecond >= lowerBound && realTimePerSecond <= upperBound) {
            return "적정";
        } else if ( realTimePerSecond > upperBound) {
            return "느림";
        } else {
            return "빠름";
        }
    }

}
