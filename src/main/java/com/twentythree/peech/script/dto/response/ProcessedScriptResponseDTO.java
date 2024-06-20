package com.twentythree.peech.script.dto.response;

import com.twentythree.peech.script.dto.OrderToParagraph;
import com.twentythree.peech.script.dto.TimePerParagraph;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class ProcessedScriptResponseDTO {
    private LocalTime expectedAllTime;
    private List<TimePerParagraph> expectedTimePerParagraphs;
    private List<OrderToParagraph> paragraphs;
}
