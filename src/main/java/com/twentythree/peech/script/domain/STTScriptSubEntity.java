package com.twentythree.peech.script.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalTime;

@DiscriminatorValue("stt")
@Entity
public class STTScriptSubEntity extends ScriptSuperEntity {
    @Column(name = "total_real_time")
    private LocalTime totalRealTime;
}

