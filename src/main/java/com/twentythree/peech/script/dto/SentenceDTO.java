package com.twentythree.peech.script.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SentenceDTO {

    private String sentenceId;
    private Long sentenceOrder;
    private String sentenceContent;

}
