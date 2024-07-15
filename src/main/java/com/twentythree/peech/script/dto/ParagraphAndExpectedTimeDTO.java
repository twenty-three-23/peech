package com.twentythree.peech.script.dto;

import com.twentythree.peech.script.stt.dto.response.NowStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class ParagraphAndExpectedTimeDTO extends ParagraphDTO {

    private LocalTime expectedTime;
    private NowStatus nowStatus;

    public ParagraphAndExpectedTimeDTO(Long paragraphId, Long paragraphOrder, List<SentenceDTO> sentences, LocalTime expectedTime) {
        super(paragraphId, paragraphOrder, sentences);
        this.expectedTime = expectedTime;
    }
}
