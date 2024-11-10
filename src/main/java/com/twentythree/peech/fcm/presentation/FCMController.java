package com.twentythree.peech.fcm.presentation;

import com.twentythree.peech.fcm.application.NotificationService;
import com.twentythree.peech.fcm.dto.request.RequestFCMTokenDTO;
import com.twentythree.peech.security.jwt.JWTAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class FCMController {

    private final NotificationService notificationService;

    @PutMapping("api/v2/user/notification/token")
    public ResponseEntity<Void> putFCMToken(RequestFCMTokenDTO request, @AuthenticationPrincipal JWTAuthentication jwtAuthentication) {

        Long userId = jwtAuthentication.getUserId();
        notificationService.saveOrUpdateToken(request, userId);

        return ResponseEntity.ok().build();
    }
}
