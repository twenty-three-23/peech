package com.twentythree.peech.fcm.entity;

import com.twentythree.peech.common.domain.BaseCreatedAtEntity;
import com.twentythree.peech.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Objects;


@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Table(name = "NOTIFICATION")
@Entity
public class NotificationEntity extends BaseCreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NotificationEntity that)) return false;
        return Objects.equals(deviceId, that.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(deviceId);
    }

    public NotificationEntity(UserEntity userEntity, String deviceId, String fcmToken) {
        this.userEntity = userEntity;
        this.deviceId = deviceId;
        this.fcmToken = fcmToken;
    }

    public static NotificationEntity ofCreateFCMToken(UserEntity userEntity, String deviceId , String fcmToken) {
        return new NotificationEntity(userEntity, deviceId, fcmToken);
    }

    public void updateToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
