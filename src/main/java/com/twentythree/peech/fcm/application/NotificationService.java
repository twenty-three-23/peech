package com.twentythree.peech.fcm.application;

import com.twentythree.peech.fcm.dto.request.RequestFCMTokenDTO;

public interface NotificationService {
    void pushNotification(Long userId);
    void saveOrUpdateToken(RequestFCMTokenDTO fcmTokenDTO, Long userId);
    void testPushNotification(Long userId);
    void deleteToken(String deviceId);
}
