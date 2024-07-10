package com.twentythree.peech.script.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class RedisSentenceDTO {

    private Long paragraphId;
    private Long paragraphOrder;
    private Long sentenceOrder;
    private String sentenceContent;
    private LocalTime time;
    private boolean isChanged;
}
