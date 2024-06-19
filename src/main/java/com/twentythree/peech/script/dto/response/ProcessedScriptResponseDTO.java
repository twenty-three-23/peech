package com.twentythree.peech.script.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.Map;

@AllArgsConstructor
@Getter
public class ProcessedScriptResponseDTO {
    private LocalTime expectedAllTime;
    private Map<Long, LocalTime> expectedTimePerParagraphs;
    private Map<Long, String> paragraphs;
}
