package com.twentythree.peech.fcm.client;


import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "FCMClient",
        url = "https://fcm.googleapis.com/v1/projects/${fcm.project-id}/messages:send"
)
public interface FCMClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    void sendNotification(
            FirebaseMessaging message
    );


}
