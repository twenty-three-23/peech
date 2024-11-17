package com.twentythree.peech.script.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnalyzeResultDTO {
    private int httpStatus;
    private String result;
}
