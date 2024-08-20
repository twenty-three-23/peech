package com.twentythree.peech.auth.service;

import com.twentythree.peech.security.jwt.JWTAuthentication;
import org.springframework.security.core.Authentication;

public class SecurityContextHolder {

    public static Long getUserId() {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        JWTAuthentication jwtAuthentication = (JWTAuthentication) authentication.getPrincipal();

        return jwtAuthentication.getUserId();
    }
}
