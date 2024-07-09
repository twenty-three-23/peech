package com.twentythree.peech.script.dto;

import java.time.LocalTime;

public record SaveRedisSentenceInfoDto(
        Long sentenceId,
        Long paragraphId,
        Long paragraphOrder,
        String sentenceContent,
        Long sentenceOrder,
        LocalTime sentenceTime,
        Boolean isChanged
) {
}
