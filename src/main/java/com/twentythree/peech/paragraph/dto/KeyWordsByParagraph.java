package com.twentythree.peech.paragraph.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class KeyWordsByParagraph {
    private Long paragraphOrder;
    private List<String> keyWords;
}
