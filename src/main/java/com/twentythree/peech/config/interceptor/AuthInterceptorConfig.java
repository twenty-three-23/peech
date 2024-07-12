package com.twentythree.peech.config.interceptor;

import com.twentythree.peech.auth.interceptor.AuthInterceptor;
import com.twentythree.peech.common.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
class AuthInterceptorConfig implements WebMvcConfigurer {

    private final JWTUtils jwtUtils;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(jwtUtils))
                .order(10)
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger-ui/**", "/v3/**");
    }
}
