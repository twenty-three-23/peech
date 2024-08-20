package com.twentythree.peech.paragraph.domain;

import com.twentythree.peech.paragraph.valueobject.Paragraph;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter(AccessLevel.PACKAGE)
@AllArgsConstructor(staticName = "of")
public class ParagraphsDomain {

    private Long scriptId;
    private List<Paragraph> paragraphs;

}
