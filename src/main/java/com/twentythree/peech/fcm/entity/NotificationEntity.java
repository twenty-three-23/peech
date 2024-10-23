package com.twentythree.peech.fcm.entity;

import com.twentythree.peech.common.domain.BaseCreatedAtEntity;
import com.twentythree.peech.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public static NotificationEntity of(Long id, UserEntity userEntity,String deviceId ,String fcmToken) {
        return new NotificationEntity(id, userEntity, deviceId,fcmToken);
    }

}
