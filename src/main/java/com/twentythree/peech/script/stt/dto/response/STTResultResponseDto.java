package com.twentythree.peech.script.stt.dto.response;

import java.time.LocalTime;
import java.util.List;

public record STTResultResponseDto(
    LocalTime realTimePerScript,
    List<Paragraph> paragraphs
) {
    public record Paragraph(
            Long paragraphId,
            List<Sentence> sentences,
            LocalTime realTimePerParagraph
    ){
        public record Sentence(
                Long sentenceId,
                String sentenceContent
        ){}
    }
}
