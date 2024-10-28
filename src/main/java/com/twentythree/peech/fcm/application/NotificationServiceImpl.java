package com.twentythree.peech.fcm.application;


import com.twentythree.peech.fcm.dto.request.RequestFCMTokenDTO;
import com.twentythree.peech.fcm.entity.NotificationEntity;
import com.twentythree.peech.fcm.event.FCMPushedEvent;
import com.twentythree.peech.fcm.infra.NotificationRepository;
import com.twentythree.peech.user.entity.UserEntity;
import com.twentythree.peech.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
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
    public void saveOrUpdateToken(RequestFCMTokenDTO request, Long userId) {
        notificationRepository.findByDeviceId(request.getDeviceId())
                .ifPresentOrElse(
                        fcmToken -> updateFCMToken(fcmToken, request.getFcmToken()),

                        () -> {
                            UserEntity userEntity = userRepository.findById(userId)
                                    .orElseThrow(() -> new IllegalStateException("유저가 존재하지 않습니다."));
                            NotificationEntity newEntity = NotificationEntity
                                    .ofCreateFCMToken(
                                            userEntity,
                                            request.getDeviceId(),
                                            request.getFcmToken()
                                    );
                            notificationRepository.save(newEntity);
                        });
    }

    private void updateFCMToken(NotificationEntity originEntity, String newToken){
        originEntity.updateToken(newToken);
    }
}
