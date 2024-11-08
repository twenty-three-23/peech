package com.twentythree.peech.auth.service;

import com.twentythree.peech.auth.dto.SecurityContextHolderDTO;
import com.twentythree.peech.security.jwt.JWTAuthentication;
import org.springframework.security.core.Authentication;

public class SecurityContextHolder {

    public static SecurityContextHolderDTO getContextHolder() {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        JWTAuthentication jwtAuthentication = (JWTAuthentication) authentication.getPrincipal();
        Long userId = jwtAuthentication.getUserId();
        String funnel = jwtAuthentication.getFunnel();

        return new SecurityContextHolderDTO(userId, funnel);
    }

    public static Long getUserId() {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        JWTAuthentication jwtAuthentication = (JWTAuthentication) authentication.getPrincipal();

        return jwtAuthentication.getUserId();
    }

    public static String getEmail() {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        JWTAuthentication jwtAuthentication = (JWTAuthentication) authentication.getPrincipal();

        return jwtAuthentication.getEmail();
    }

    public static String getServiceType() {
        Authentication authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        JWTAuthentication jwtAuthentication = (JWTAuthentication) authentication.getPrincipal();

        return jwtAuthentication.getServiceType();
    }
}
