package com.twentythree.peech.fcm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RequestFCMTokenDTO {
    private String deviceId;
    private String fcmToken;
}
