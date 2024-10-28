package com.twentythree.peech.script.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ParagraphExpectedTimeDTO {

    private LocalTime time;
    private String paragraph;

}
