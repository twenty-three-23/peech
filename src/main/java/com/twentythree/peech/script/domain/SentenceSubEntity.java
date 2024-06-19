package com.twentythree.peech.script.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalTime;

@Entity
@DiscriminatorValue("input")
public class SentenceSubEntity extends SentenceSuperEntity {
    @Column(name = "sentence_expected_time")
    private LocalTime sentenceExpectedTime;
}
