package com.twentythree.peech.auth.service;

import com.twentythree.peech.common.utils.JWTUtils;
import com.twentythree.peech.common.utils.UserRoleConvertUtils;
import com.twentythree.peech.security.jwt.JWTAuthentication;
import com.twentythree.peech.user.dto.AccessAndRefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReissueAccessAndRefreshTokenService {

    private final JWTUtils jwtUtils;

    public AccessAndRefreshToken createNewToken(String refreshToken) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JWTAuthentication jwtAuthentication = (JWTAuthentication) authentication.getPrincipal();

        Long userId = jwtAuthentication.getUserId();
        GrantedAuthority Authority = authentication.getAuthorities()
                .stream().findFirst().orElseThrow(() -> new NoSuchElementException("No authorities found for the user"));

        String newAccessToken = jwtUtils.createAccessToken(userId, UserRoleConvertUtils
                                                                .convertStringToUserRole(Authority.getAuthority()));
        String newRefreshToken = jwtUtils.createRefreshToken(userId, UserRoleConvertUtils
                                                                .convertStringToUserRole(Authority.getAuthority()));

        return new AccessAndRefreshToken(newAccessToken, newRefreshToken);
    }
}


