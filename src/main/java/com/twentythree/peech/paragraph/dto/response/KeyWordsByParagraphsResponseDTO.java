package com.twentythree.peech.paragraph.dto.response;

import com.twentythree.peech.paragraph.dto.KeyWordsByParagraph;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class KeyWordsByParagraphsResponseDTO {

    private List<KeyWordsByParagraph> paragraphs;
}
