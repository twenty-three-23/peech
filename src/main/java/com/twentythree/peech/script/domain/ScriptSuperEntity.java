package com.twentythree.peech.script.domain;

import com.twentythree.peech.common.domain.BaseCreatedAtEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "SCRIPT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DType")
@Getter
public class ScriptSuperEntity extends BaseCreatedAtEntity {
    @Id
    @Column(name = "script_id")
    private Long scriptId;

    @JoinColumns(value = {
            @JoinColumn(name = "major_vesion"),
            @JoinColumn(name = "minor_version")
    })
    private VersionPk versionFk;

    @Column(name = "script_content", nullable = false )
    private String scriptContent;


}
