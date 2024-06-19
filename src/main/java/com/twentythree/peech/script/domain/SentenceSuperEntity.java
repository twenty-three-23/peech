package com.twentythree.peech.script.domain;

import com.twentythree.peech.common.domain.BaseCreatedAtEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DType")
@Getter
public class SentenceSuperEntity extends BaseCreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sentence_id", nullable = false)
    private Long sentenceId;

    @ManyToOne
    @JoinColumn(name = "script_id")
    private ScriptSuperEntity scriptSuperEntity;

    @Column(name = "paragraph_id", nullable = false)
    private Long paragraphId;

    @Column(name = "sentence_content", nullable = false)
    private String sentenceContent;

    @Column(name = "sentence_order", nullable = false)
    private Long sentenceOrder;
}
