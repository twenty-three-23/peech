package com.twentythree.peech.script.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@Data
public class ModifiedParagraphDTO {
    private Long paragraphId;
    private Long paragraphOrder;
    private LocalTime time;
    private boolean isCalculated;
    private List<SentenceDTO> sentences;
}
