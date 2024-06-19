package com.twentythree.peech.script.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalTime;

@DiscriminatorValue("stt")
@Entity
public class STTScriptSubEntity extends ScriptSuperEntity {
    private LocalTime total_real_time;
}

