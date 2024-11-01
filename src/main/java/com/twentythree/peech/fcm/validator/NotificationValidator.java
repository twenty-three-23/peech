package com.twentythree.peech.fcm.validator;

import com.twentythree.peech.fcm.infra.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class NotificationValidator {

    private final NotificationRepository notificationRepository;

    public boolean existTokenByDeviceId(String deviceId) {
        return notificationRepository.findNotificationByDeviceId(deviceId).isPresent();
    }
}
