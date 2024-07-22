package com.twentythree.peech.script.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ModifiedParagraphDTO {
    private Long paragraphId;
    private Long paragraphOrder;
    private LocalTime time;
    private NowStatus nowStatus;
    private List<SentenceDTO> sentences;
}
