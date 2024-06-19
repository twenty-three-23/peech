package com.twentythree.peech.script.domain;

import jakarta.persistence.*;

import java.time.LocalTime;

@DiscriminatorValue("input")
@Entity
public class ScriptSubEntity extends ScriptSuperEntity {
    private LocalTime total_expect_time;
}
