package com.twentythree.peech.fcm.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FCMPushedEvent {
    private List<String> fcmTokenList;
}
