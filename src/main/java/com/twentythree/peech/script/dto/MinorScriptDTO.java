package com.twentythree.peech.script.dto;

import java.time.LocalDate;

public record MinorScriptDTO(Long minorVersion, String scriptContent, LocalDate createdAt) {
}
