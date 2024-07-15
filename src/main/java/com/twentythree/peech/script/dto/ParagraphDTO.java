package com.twentythree.peech.script.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ParagraphDTO {

    private Long paragraphId;
    private Long paragraphOrder;
    private List<SentenceDTO> sentences;

}
