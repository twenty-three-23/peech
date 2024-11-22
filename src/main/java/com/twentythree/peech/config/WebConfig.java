package com.twentythree.peech.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${spring.cors.allowed-origins}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("allowedOrigins") // 허용할 도메인
                .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS") // 허용할 HTTP method
                .allowCredentials(true); // 쿠키 인증 요청 허용
    }
}
