package com.twentythree.peech.script.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
public class ParagraphsResponseDTO {
    private Map<Long, String> paragraphs;
}
