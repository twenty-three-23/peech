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

    // @ManyToOne(fetch = FetchType.LAZY) package entity가 완성되면 packageId에 @OneToMany로 연결
    @JoinColumn
    @Column(name = "package_id", nullable = false)
    private Long packageId;

    public static VersionEntity of(Long majorVersion, Long minorVersion, Long packageId) {
        return new VersionEntity(majorVersion, minorVersion, packageId);
    }

}
