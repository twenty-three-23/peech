package com.twentythree.peech.paragraph.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class HistoryParagraphDTO {

    private Long paragraphOrder;
    private String measurementResult;
    private LocalTime time;
    private String paragraph;

}
