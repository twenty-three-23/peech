package com.twentythree.peech.security.config;

import com.twentythree.peech.security.filter.JWTAuthenticationFilter;
import com.twentythree.peech.security.handler.JWTAuthAccessDeniedHandler;
import com.twentythree.peech.security.handler.JWTAuthEntryPoint;
import com.twentythree.peech.security.jwt.JWTAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final JWTAuthEntryPoint jwtAuthEntryPoint;
    private final JWTAuthAccessDeniedHandler jwtAuthAccessDeniedHandler;

    public SecurityConfig(JWTAuthenticationFilter jwtAuthenticationFilter,
                          JWTAuthEntryPoint jwtAuthEntryPoint,
                          JWTAuthAccessDeniedHandler jwtAuthAccessDeniedHandler
                          ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
        this.jwtAuthAccessDeniedHandler = jwtAuthAccessDeniedHandler;
    }

    // 필터체인 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        String[] swagger = { "/swagger-ui/**", "/v3/api-docs/**"};
        String[] defaultPermitAll = {"/api/v1.1/auth/reissue","/actuator","/error", "/api/v1.1/app"};

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers(swagger).permitAll()
                                .requestMatchers(defaultPermitAll).permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/v1.1/user").permitAll()
                                .anyRequest().authenticated())
                .exceptionHandling(e -> e.
                        authenticationEntryPoint(jwtAuthEntryPoint)
                .accessDeniedHandler(jwtAuthAccessDeniedHandler))
                .addFilterBefore(jwtAuthenticationFilter, ExceptionTranslationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
