package com.twentythree.peech.script.domain;

import com.twentythree.peech.common.domain.BaseCreatedAtEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(VersionPk.class)
@Table(name="VERSION")
@Entity
public class VersionEntity extends BaseCreatedAtEntity {

    @Id
    private Long majorVersion;
    @Id
    private Long minorVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "theme_id")
    private ThemeEntity ThemeEntity;

    public static VersionEntity of(Long majorVersion, Long minorVersion, ThemeEntity ThemeEntity) {
        return new VersionEntity(majorVersion, minorVersion, ThemeEntity);
    }

}
