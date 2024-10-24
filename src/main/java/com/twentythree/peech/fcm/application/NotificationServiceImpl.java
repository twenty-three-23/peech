package com.twentythree.peech.fcm.application;

import com.twentythree.peech.fcm.entity.NotificationEntity;
import com.twentythree.peech.fcm.event.FCMPushedEvent;
import com.twentythree.peech.fcm.event.FCMTokenEvent;
import com.twentythree.peech.fcm.infra.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void pushNotification(Long userId) {
        List<String> fcmTokenList = notificationRepository.findAllByUserId(userId);

        if(fcmTokenList.isEmpty()){
            throw new IllegalStateException("FCM 토큰이 존재하지 않습니다.");
        }

        applicationEventPublisher.publishEvent(new FCMPushedEvent(fcmTokenList));
    }

    @Transactional
    @Override
    public void saveOrUpdateToken(FCMTokenEvent fcmTokenEvent, boolean result) {
        if(!result){
            NotificationEntity notificationEntity = NotificationEntity
                    .of(fcmTokenEvent.getUserEntity(), fcmTokenEvent.getDeviceId(), fcmTokenEvent.getFcmToken());
            notificationRepository.save(notificationEntity);

        }else {
            notificationRepository.updateTokenByDeviceId(fcmTokenEvent.getFcmToken(), fcmTokenEvent.getDeviceId());
        }
    }
}
