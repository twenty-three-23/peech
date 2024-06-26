package com.twentythree.peech.script.dto;

import com.twentythree.peech.script.domain.ScriptEntity;

import java.time.LocalTime;

public record SaveScriptDTO(ScriptEntity scriptEntity, LocalTime expectedTime) {
}
