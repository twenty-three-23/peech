package com.twentythree.peech.auth.config;

import com.twentythree.peech.auth.resolver.AuthArgumentResolver;
import com.twentythree.peech.common.utils.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Deprecated
@RequiredArgsConstructor
@Configuration
public class ArgumentResolverConfig implements WebMvcConfigurer {

    private final JWTUtils jwtUtils;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthArgumentResolver(jwtUtils));
    }
}
