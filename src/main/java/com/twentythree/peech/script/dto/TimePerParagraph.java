package com.twentythree.peech.script.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class TimePerParagraph {
    private Long paragraphId;
    private LocalTime time;
}
