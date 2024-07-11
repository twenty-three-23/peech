package com.twentythree.peech.script.domain;

import com.twentythree.peech.common.domain.BaseCreatedAtEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(VersionPk.class)
@Table(name = "VERSION")
@Entity
public class VersionEntity extends BaseCreatedAtEntity {

    @Id
    @Column(name = "major_version")
    private Long majorVersion;
    @Id
    @Column(name = "minor_version")
    private Long minorVersion;
    @Id
    @Column(name = "theme_id")
    private Long themeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "theme_id", insertable = false, updatable = false)
    private ThemeEntity ThemeEntity;

    public static VersionEntity of(Long majorVersion, Long minorVersion, Long themeId, ThemeEntity ThemeEntity) {
        return new VersionEntity(majorVersion, minorVersion, themeId, ThemeEntity);
    }

    public static VersionEntity ofCreateInputScriptVersion(Long latestMajorVersion, Long themeId, ThemeEntity ThemeEntity) {
        if (latestMajorVersion == null) {
            return VersionEntity.of(1L, 0L, themeId, ThemeEntity);
        } else {
            return VersionEntity.of(latestMajorVersion + 1L, 0L, themeId, ThemeEntity);
        }
    }

}
