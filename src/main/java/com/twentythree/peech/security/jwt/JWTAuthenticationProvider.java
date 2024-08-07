package com.twentythree.peech.security.jwt;

import com.twentythree.peech.user.service.UserService;
import org.apache.commons.lang3.ClassUtils;
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

        userService.existUserById(userId);

        return new JWTAuthenticationToken(new JWTAuthentication(userId),
                AuthorityUtils.createAuthorityList("ROLE_USER"));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ClassUtils.isAssignable(JWTAuthenticationToken.class, authentication);
    }

}
