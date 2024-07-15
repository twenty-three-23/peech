package com.twentythree.peech.script.stt.dto;

import com.twentythree.peech.script.domain.SentenceEntity;

import java.time.LocalTime;

public record SaveSTTSentenceVO(
        SentenceEntity sentenceEntity,
        LocalTime realTime
) {
}
