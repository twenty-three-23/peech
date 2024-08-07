package com.twentythree.peech.script.stt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SentenceDTO {

    private String sentenceId;
    private Long sentenceOrder;
    private String sentenceContent;

}
