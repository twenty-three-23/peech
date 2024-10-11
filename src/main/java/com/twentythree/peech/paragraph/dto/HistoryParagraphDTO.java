package com.twentythree.peech.script.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryParagraphDTO {

    private Long paragraphId;
    private String measurementResult;
    private LocalTime time;
    private String paragraph;

}
