package com.twentythree.peech.security.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JWTAuthentication {
    Long userId;
    String email;
    String serviceType;
    String funnel;

    public JWTAuthentication(Long userId, String email, String serviceType, String funnel) {
        this.userId = userId;
        this.email = email;
        this.serviceType = (serviceType != null) ? serviceType : "InWeb";
        this.funnel = funnel;
    }

    public static JWTAuthentication ofPending(String serviceType){
        return new JWTAuthentication(null, null, serviceType, null);
    }
}

