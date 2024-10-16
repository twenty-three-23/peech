package com.twentythree.peech.script.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter

@AllArgsConstructor
@NoArgsConstructor
public class ScriptDTO {

    private Long scriptId;
    private String scriptContent;
    private LocalDateTime createdAt;

}
