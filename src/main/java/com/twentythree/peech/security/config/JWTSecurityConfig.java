package com.twentythree.peech.security.config;

import com.twentythree.peech.common.utils.JWTUtils;
import com.twentythree.peech.security.filter.JWTAuthenticationFilter;
import com.twentythree.peech.security.handler.JWTAuthAccessDeniedHandler;
import com.twentythree.peech.security.jwt.JWTUserDetailsService;
import com.twentythree.peech.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JWTSecurityConfig {

    private final JWTUtils jwtUtils;
    private final UserService userService;
    private final JWTUserDetailsService jwtUserDetailsService;

    public JWTSecurityConfig(JWTUtils jwtUtils, JWTUserDetailsService jwtUserDetailsService, UserService userService) {
        this.jwtUtils = jwtUtils;
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.userService = userService;
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter(jwtUtils, jwtUserDetailsService);
    }

    @Bean
    public JWTAuthAccessDeniedHandler authAccessDeniedHandler() {
        return new JWTAuthAccessDeniedHandler();
    }

}
