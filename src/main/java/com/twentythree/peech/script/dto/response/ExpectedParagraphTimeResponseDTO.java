package com.twentythree.peech.script.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ExpectedParagraphTimeResponseDTO {
    private Map<Long, LocalTime> expectedTimePerParagraphs;
}
