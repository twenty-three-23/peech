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
@Entity
public class VersionEntity extends BaseCreatedAtEntity {

    @Id
    private Long majorVersion;
    @Id
    private Long minorVersion;

    // @ManyToOne(fetch = FetchType.LAZY) package entity가 완성되면 packageId에 @OneToMany로 연결
    // @JoinedColnum
    @Column(nullable = false)
    private Long packageId;

    @Column(nullable = false)
    private Long userId;

    public static VersionEntity of(Long majorVersion, Long minorVersion, Long packageId, Long userId) {
        return new VersionEntity(majorVersion, minorVersion, packageId, userId);
    }

}
