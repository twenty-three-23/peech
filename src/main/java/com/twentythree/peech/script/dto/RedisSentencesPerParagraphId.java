package com.twentythree.peech.script.dto;

import lombok.Data;

import java.util.List;

@Data
public class RedisSentencesPerParagraphId {
    private Long paragraphId;
    private List<RedisSentenceDTO> sentences;
}
