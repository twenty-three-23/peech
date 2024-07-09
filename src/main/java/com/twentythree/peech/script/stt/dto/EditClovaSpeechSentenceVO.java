package com.twentythree.peech.script.stt.dto;

// clova speech api를 거친 결과를 문장 단위로 수정해주는 DTO

import java.time.LocalTime;

public record EditClovaSpeechSentenceVO(
        Long sentenceOrder,
        String sentenceContent,
        LocalTime sentenceDuration
) {
}
