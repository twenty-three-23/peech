package com.twentythree.peech.fcm.application;

import com.twentythree.peech.fcm.event.FCMTokenEvent;

public interface NotificationService {
    void pushNotification(Long userId);
    void saveOrUpdateToken(FCMTokenEvent fcmTokenEvent, boolean result);
}
