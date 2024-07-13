package com.twentythree.peech.script.stt.dto;

import com.twentythree.peech.script.domain.ScriptEntity;

import java.time.LocalTime;

public record SaveSTTScriptVO(
        ScriptEntity scriptEntity,
        LocalTime realTime
) {
}
