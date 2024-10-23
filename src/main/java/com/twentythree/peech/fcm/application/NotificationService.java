package com.twentythree.peech.fcm.application;

import com.twentythree.peech.aop.annotation.Trace;
import com.twentythree.peech.fcm.event.FCMPushedEvent;
import com.twentythree.peech.fcm.infra.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Trace
    public void pushNotification(Long userId) {
//        List<String> fcmTokenList = notificationRepository.findAllByUserId(userId);
//
//        if(fcmTokenList.isEmpty()){
//            throw new IllegalStateException("FCM 토큰이 존재하지 않습니다.");
//        }

        List<String> fcmTokenList = List.of("testFcmToken1", "testFcmToken2", "testFcmToken3");

        applicationEventPublisher.publishEvent(new FCMPushedEvent(fcmTokenList));
    }
}
