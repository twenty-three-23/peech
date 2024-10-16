package com.twentythree.peech.script.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MajorScriptDTO {

    private Long scriptId;
    private Long majorVersion;
    private String scriptContent;
    private LocalDateTime createdAt;
    private int minorScriptsCount;

}
