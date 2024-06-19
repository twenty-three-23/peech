package com.twentythree.peech.script.domain;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalTime;
@Entity
@DiscriminatorValue("stt")
public class STTSentenceSubEntity extends SentenceSuperEntity{
    @Column(name = "sentence_real_time")
    private LocalTime sentenceRealTime;
}
