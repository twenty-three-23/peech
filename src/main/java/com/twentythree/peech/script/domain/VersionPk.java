package com.twentythree.peech.script.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class VersionPk implements Serializable {
    private Long majorVersion;
    private Long minorVersion;
    private Long themeEntity;
}
