package com.twentythree.peech.script.domain;

import jakarta.persistence.*;

import java.time.LocalTime;

@DiscriminatorValue("input")
@Entity
public class ScriptSubEntity extends ScriptSuperEntity {
    @Column(name = "total_expect_time")
    private LocalTime totalExpectTime;
}
