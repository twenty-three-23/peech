package com.twentythree.peech.fcm.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.io.InputStream;


@Configuration
public class FcmConfig {

    @Value("${fcm.key-path}")
    private String fcmKeyPath;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    private void init() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            log.info("FirebaseApp을 초기화합니다");
            InputStream firebaseAccount = new ClassPathResource(fcmKeyPath).getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(firebaseAccount))
                    .build();

            FirebaseApp.initializeApp(options);
        }
    }

}