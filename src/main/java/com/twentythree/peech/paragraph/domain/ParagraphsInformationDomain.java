package com.twentythree.peech.paragraph.domain;

import com.twentythree.peech.paragraph.valueobject.Paragraph;
import com.twentythree.peech.paragraph.valueobject.ParagraphInformation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter(AccessLevel.PACKAGE)
@AllArgsConstructor(staticName = "of")
public class ParagraphsInformationDomain {

    private Long scriptId;
    private List<ParagraphInformation> paragraphInformations;
}
