package com.twentythree.peech.paragraph.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class ParagraphInformation {

    private Long paragraphOrder;
    private String paragraphContent;
    private LocalTime paragraphExpectedTime;
    private LocalTime paragraphRealTime;

}
