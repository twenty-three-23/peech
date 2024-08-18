package com.twentythree.peech.auth.service;

import com.twentythree.peech.common.utils.JWTUtils;
import com.twentythree.peech.common.utils.UserRoleConvertUtils;
import com.twentythree.peech.security.exception.JWTAuthenticationException;
import com.twentythree.peech.security.exception.LoginExceptionCode;
import com.twentythree.peech.security.jwt.JWTAuthenticationToken;
import com.twentythree.peech.user.dto.AccessAndRefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReissueAccessAndRefreshTokenService {

    private final JWTUtils jwtUtils;

    public AccessAndRefreshToken createNewToken(String refreshToken) {

        try {
            JWTAuthenticationToken authentication = (JWTAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

            Long userId = authentication.getPrincipal().getUserId();
            GrantedAuthority Authority = authentication.getAuthorities()
                    .stream().findFirst().orElseThrow(() -> new NoSuchElementException("유저의 권한이 부여되지 않았습니다"));

            String newAccessToken = jwtUtils.createAccessToken(userId, UserRoleConvertUtils
                    .convertStringToUserRole(Authority.getAuthority()));
            String newRefreshToken = jwtUtils.createRefreshToken(userId, UserRoleConvertUtils
                    .convertStringToUserRole(Authority.getAuthority()));

            return new AccessAndRefreshToken(newAccessToken, newRefreshToken);
        }catch (Exception e){
            throw new JWTAuthenticationException(LoginExceptionCode.LOGIN_EXCEPTION_CODE);
        }
    }
}


