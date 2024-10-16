package com.twentythree.peech.script.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ThemeDTO {

    private Long themeId;
    private String themeTitle;
    private LocalDateTime createdAt;
    private int majorVersionCnt;

}
