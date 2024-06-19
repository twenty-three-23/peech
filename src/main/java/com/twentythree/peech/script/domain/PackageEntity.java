package com.twentythree.peech.script.domain;


import com.twentythree.peech.common.domain.BaseCreatedAtEntity;
import com.twentythree.peech.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PACKAGE")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PackageEntity extends BaseCreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id", nullable = false)
    private Long packageId;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity userEntity;

    @Column(name = "package_title", nullable = true)
    private String packageTitle;

}

