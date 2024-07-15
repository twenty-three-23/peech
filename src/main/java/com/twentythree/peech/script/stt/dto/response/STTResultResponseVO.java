package com.twentythree.peech.script.stt.dto.response;

import java.time.LocalTime;
import java.util.List;

// 클라이언트에게 전달할 STT 결과 응답 VO
public record STTResultResponseVO(
        LocalTime realTimePerScript,
        List<STTResultResponseDto.Paragraph> paragraphs
) {
    public record Paragraph(
            int paragraphId,
            List<STTResultResponseDto.Paragraph.Sentence> sentences,
            LocalTime realTimePerParagraph
    ){
        public record Sentence(
                int sentenceId,
                String sentenceContent
        ){}
    }
}