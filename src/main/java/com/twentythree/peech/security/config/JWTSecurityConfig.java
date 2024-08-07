package com.twentythree.peech.security.config;

import com.twentythree.peech.common.utils.JWTUtils;
import com.twentythree.peech.security.filter.JWTAuthenticationFilter;
import com.twentythree.peech.security.handler.JWTAuthAccessDeniedHandler;
import com.twentythree.peech.security.jwt.JWTAuthenticationProvider;
import com.twentythree.peech.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTSecurityConfig {

    private final JWTUtils jwtUtils;
    private final UserService userService;

    public JWTSecurityConfig(JWTUtils jwtUtils, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter(jwtUtils);
    }

    @Bean
    public JWTAuthenticationProvider jwtAuthenticationProvider() {
        return new JWTAuthenticationProvider(userService);
    }

    @Bean
    public JWTAuthAccessDeniedHandler authAccessDeniedHandler() {
        return new JWTAuthAccessDeniedHandler();
    }

}
