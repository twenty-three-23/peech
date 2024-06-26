package com.twentythree.peech.script.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ThemeDTO {

    private Long themeId;
    private String themeTitle;
    private LocalDate createdAt;
    private int majorVersionCnt;

}
