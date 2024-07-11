package com.twentythree.peech.script.stt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class STTResultSentenceDto {
    private Long sentenceId;
    private String content;
    private int realTime;
}