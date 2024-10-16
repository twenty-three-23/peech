package com.twentythree.peech.script.dto;

import java.time.LocalDateTime;

public record MinorScriptDTO(Long minorVersion, String scriptContent, LocalDateTime createdAt) {
}
