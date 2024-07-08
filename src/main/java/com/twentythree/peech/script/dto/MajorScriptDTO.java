package com.twentythree.peech.script.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class MajorScriptDTO {

    private Long scriptId;
    private Long majorVersion;
    private String scriptContent;
    private LocalDate createdAt;
    private int minorScriptsCount;

}
