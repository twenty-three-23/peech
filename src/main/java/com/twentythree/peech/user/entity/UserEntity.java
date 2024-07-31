package com.twentythree.peech.user.entity;

import com.twentythree.peech.common.domain.BaseTimeEntity;
import com.twentythree.peech.user.value.*;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER")
@Entity
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Embedded
    private AuthorizationIdentifier authorizationIdentifier;

    @Column(name = "device_id", unique=true)
    private String deviceId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth")
    private LocalDate birth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private UserGender gender;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nick_name")
    private String nickName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false) @ColumnDefault("'ROLE_COMMON'")
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false) @ColumnDefault("'ACTIVE'")
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "sign_up_finished", nullable = false)
    private SignUpFinished signUpFinished;
    
    @Column(name = "delete_at")
    private LocalDate deleteAt;


    public UserEntity(String device_id) {
        this.deviceId = device_id;
    }

    public static UserEntity ofNoLogin(String device_id) {
        return new UserEntity(device_id);
    }


    public static UserEntity of(Long id, String deviceId, AuthorizationIdentifier authorizationIdentifier, String firstName, String lastName, LocalDate birth, UserGender gender, String email, String nickName, SignUpFinished signUpFinished) {
        return new UserEntity(id, authorizationIdentifier, deviceId, firstName, lastName, birth, gender, email, nickName, UserRole.ROLE_COMMON, UserStatus.ACTIVE, signUpFinished, null );
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
