package com.twentythree.peech.meta.util;

import com.twentythree.peech.meta.conversionapi.eventhandler.event.MetaSecret;
import com.twentythree.peech.meta.dto.DeviceDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

@Component
public class MetaUtil {

    private static String webPixelId;
    private static String webAccessToken;
    private static String appPixelId;
    private static String appAccessToken;

    @Value("${meta.web.pixel-id}")
    public void setWebPixelId(String webPixelId) {
        MetaUtil.webPixelId = webPixelId;
    }
    @Value("${meta.web.access-token}")
    public void setWebAccessToken(String webAccessToken) {
        MetaUtil.webAccessToken = webAccessToken;
    }
    @Value("${meta.app.pixel-id}")
    public void setAppPixelId(String appPixelId) {
        MetaUtil.appPixelId = appPixelId;
    }
    @Value("${meta.app.access-token}")
    public void setAppAccessToken(String appAccessToken) {
        MetaUtil.appAccessToken = appAccessToken;
    }

    public static String encryptSHA256(String userInformation) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(userInformation.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found!", e);
        }
    }

    public static long getEventTime() {
        return Instant.now().getEpochSecond();
    }

    public static DeviceDTO getDeviceTypeAndMetaSecret(String serviceType) {
        if(serviceType.equals("InApp")) {
            return new DeviceDTO("app", new MetaSecret(appPixelId, appAccessToken));
        } else {
            return new DeviceDTO("website", new MetaSecret(webPixelId, webAccessToken));
        }
    }

}
