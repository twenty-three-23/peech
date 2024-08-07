package com.twentythree.peech.security.config;

import com.twentythree.peech.security.filter.JWTAuthenticationFilter;
import com.twentythree.peech.security.filter.JWTExceptionFilter;
import com.twentythree.peech.security.handler.JWTAuthAccessDeniedHandler;
import com.twentythree.peech.security.handler.JWTAuthEntryPoint;
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
    private final JWTAuthEntryPoint JWTAuthEntryPoint;
    private final JWTAuthAccessDeniedHandler JWTAuthAccessDeniedHandler;

    public SecurityConfig(JWTAuthenticationFilter jwtAuthenticationFilter,
                          JWTAuthEntryPoint JWTAuthEntryPoint,
                          JWTAuthAccessDeniedHandler JWTAuthAccessDeniedHandler
                          ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.JWTAuthEntryPoint = JWTAuthEntryPoint;
        this.JWTAuthAccessDeniedHandler = JWTAuthAccessDeniedHandler;
    }

    // 필터체인 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/api/v1.1/auth/reissue").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/v1.1/user").permitAll()
                                .requestMatchers("/swagger-ui/").hasAuthority("ROLE_ADMIN")
                                .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, ExceptionTranslationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e.authenticationEntryPoint(JWTAuthEntryPoint)
                        .accessDeniedHandler(JWTAuthAccessDeniedHandler))

                .httpBasic(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable);

        return http.build();
    }
}
