package com.twentythree.peech.security.jwt;

import com.twentythree.peech.user.entity.UserEntity;
import com.twentythree.peech.user.service.UserService;
import com.twentythree.peech.user.value.SignUpFinished;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;

public class JWTAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    public JWTAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return processOAuthAuthentication(Long.parseLong(authentication.getPrincipal().toString()));
    }

    private Authentication processOAuthAuthentication(Long userId) {

        UserEntity userEntity = userService.existUserById(userId);

        if(userEntity.getSignUpFinished() == SignUpFinished.FINISHED) {
            return new JWTAuthenticationToken(new JWTAuthentication(userId),
                    AuthorityUtils.createAuthorityList("ROLE_COMMON"));
        } else {
            throw new AccessDeniedException("회원가입이 완료되지않은 사용자입니다.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ClassUtils.isAssignable(JWTAuthenticationToken.class, authentication);
    }

}
