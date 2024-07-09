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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "theme_id")
    private ThemeEntity ThemeEntity;

    public static VersionEntity of(Long majorVersion, Long minorVersion, ThemeEntity ThemeEntity) {
        return new VersionEntity(majorVersion, minorVersion, ThemeEntity);
    }

    public static VersionEntity ofCreateInputScriptVersion(Long latestMajorVersion, ThemeEntity ThemeEntity) {
        if (latestMajorVersion == null) {
            return VersionEntity.of(1L, 0L, ThemeEntity);
        } else {
            return VersionEntity.of(latestMajorVersion + 1L, 0L, ThemeEntity);
        }
    }

    // 대본을 입력한 후 STTScript를 생성할 때 사용
    public static VersionEntity ofCreateSTTScriptVersion(Long majorVersion, Long minorVersion, ThemeEntity ThemeEntity) {
        return VersionEntity.of(majorVersion, minorVersion + 1L, ThemeEntity);
    }

    // 대본 없이 바로 STTScript를 생성할 때 사용
    public static VersionEntity ofCreateSTTScriptVersion(ThemeEntity ThemeEntity) {
        return VersionEntity.of(1L, 0L, ThemeEntity);
    }

}
