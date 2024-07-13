package com.twentythree.peech.script.dto.response;

import com.twentythree.peech.script.dto.ParagraphContent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ParagraphsResponseDTO {
    private List<ParagraphContent> paragraphs;
}
