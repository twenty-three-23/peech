package com.twentythree.peech.script.dto;

import com.twentythree.peech.script.domain.ScriptEntity;

import java.time.LocalTime;

public record SaveScripDTO(ScriptEntity scriptEntity, LocalTime expectedTime) {
}
