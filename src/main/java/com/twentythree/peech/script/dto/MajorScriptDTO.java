package com.twentythree.peech.script.dto;

import java.time.LocalDate;

public record MajorScriptDTO(Long scriptId, Long majorVersion, String scriptContent, LocalDate createdAt) {
}
