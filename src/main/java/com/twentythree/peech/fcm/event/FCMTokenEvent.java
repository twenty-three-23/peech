package com.twentythree.peech.fcm.event;

import com.twentythree.peech.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FCMTokenEvent {
    private UserEntity userEntity;
    private String deviceId;
    private String fcmToken;
}
