package com.twentythree.peech.script.stt.dto;

// clova speech api를 거친 결과를 문장 단위로 수정해주는 DTO

import java.time.LocalTime;

public record AddSentenceInformationVO(
        Long sentenceOrder,
        String sentenceContent,
        int sentenceDuration,
        LocalTime startTime,
        LocalTime endTime

) {
}
