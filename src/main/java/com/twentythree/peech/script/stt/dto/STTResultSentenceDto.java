package com.twentythree.peech.script.stt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class STTResultSentenceDto {
    private Long sentenceId;
    private Long sentenceOrder;
    private String content;
    private int realTime;
}