package com.twentythree.peech.fcm.presentation;

import com.twentythree.peech.fcm.application.NotificationService;
import com.twentythree.peech.fcm.dto.request.RequestDeviceIdDTO;
import com.twentythree.peech.fcm.dto.request.RequestFCMTokenDTO;
import com.twentythree.peech.security.jwt.JWTAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FCMController {

    private final NotificationService notificationService;

    @PutMapping("api/v2/user/notification/token")
    public ResponseEntity<Void> putFCMToken(@RequestBody RequestFCMTokenDTO request, @AuthenticationPrincipal JWTAuthentication jwtAuthentication) {

        Long userId = jwtAuthentication.getUserId();
        notificationService.saveOrUpdateToken(request, userId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/v2/notification/test")
    public ResponseEntity<String> testNotification(@AuthenticationPrincipal JWTAuthentication jwtAuthentication){
        Long userId = jwtAuthentication.getUserId();
        notificationService.testPushNotification(userId);

        return ResponseEntity.ok("요청 처리 완료");
    }

    @DeleteMapping("/api/v2/user/notification/token")
    public ResponseEntity<Void> deleteFCMToken(@RequestBody RequestDeviceIdDTO request){
        notificationService.deleteToken(request.getDeviceId());

        return ResponseEntity.ok().build();
    }
}
