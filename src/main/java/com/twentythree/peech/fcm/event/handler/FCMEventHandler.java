package com.twentythree.peech.fcm.event.handler;

import com.google.firebase.ErrorCode;
import com.google.firebase.messaging.*;
import com.twentythree.peech.fcm.event.FCMPushedEvent;
import com.twentythree.peech.fcm.event.FCMTestPushEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FCMEventHandler {

    static final String title = "분석이 완료되었습니다";
    static final String body = "히스토리로 이동하여 분석결과를 확인하세요";
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Async
    @EventListener
    public void sendMessage(FCMPushedEvent fcmEventDTO) throws FirebaseMessagingException {
        // 유저가 접속한 모든 기기에 완료 푸시 알림을 보냄
        MulticastMessage message = MulticastMessage.builder()
                .putData("title", title)
                .putData("body", body)
                .addAllTokens(fcmEventDTO.getFcmTokenList())
                .build();

        BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
        log.info("messsage 전송완료");

        //Todo: 실패한 푸시 알림에 대한 처리
        response.getResponses().stream()
                .filter(res -> !res.isSuccessful())
                .filter(res -> res.getException().getErrorCode() == ErrorCode.INVALID_ARGUMENT)
                .forEach(res -> {
                    log.error("Failed to send message to: id = {} {}", res.getException().getMessagingErrorCode(), res.getException().getMessage());
                });

    }

    @EventListener
    public void testPush(FCMTestPushEvent fcmTokenList) throws FirebaseMessagingException {

        MulticastMessage message = MulticastMessage.builder()
                .putData("title", "테스트 푸시 알림")
                .putData("body", "테스트 푸시 알림을 보냅니다.")
                .addAllTokens(fcmTokenList.getFcmTokenList())
                .build();

        BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);
        log.info("test messsage 전송완료");

        //Todo: 실패한 푸시 알림에 대한 처리
        response.getResponses().stream()
                .filter(res -> !res.isSuccessful())
                .filter(res -> res.getException().getErrorCode() == ErrorCode.INVALID_ARGUMENT)
                .forEach(res -> {
                    log.error("Failed to send message to: id = {} {}", res.getMessageId(), res.getException().getMessage());
                });
    }

}
