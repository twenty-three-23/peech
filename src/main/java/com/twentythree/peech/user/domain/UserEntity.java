package com.twentythree.peech.user.domain;

import com.twentythree.peech.common.domain.BaseTimeEntity;
import com.twentythree.peech.script.domain.PackageEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "USER")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "device_id", unique=true)
    private String deviceId;

    @OneToMany(mappedBy = "userEntity")
    private List<PackageEntity> packages;

    public UserEntity(String device_id) {
        this.deviceId = device_id;
    }

    public static UserEntity ofNoLogin(String device_id) {
        return new UserEntity(device_id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
