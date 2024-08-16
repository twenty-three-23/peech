package com.twentythree.peech.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twentythree.peech.common.dto.ErrorDTO;
import com.twentythree.peech.security.exception.JWTAuthenticationException;
import com.twentythree.peech.security.exception.LoginExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JWTAuthEntryPoint implements AuthenticationEntryPoint {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        JWTAuthenticationException jwtAuthenticationException = (JWTAuthenticationException) request.getAttribute("JWTAuthenticationException");

        if(jwtAuthenticationException != null) {
            ErrorDTO errorDTO = new ErrorDTO(jwtAuthenticationException.getMessage());
            response.setStatus(jwtAuthenticationException.getStatus().value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(errorDTO));
            response.getWriter().flush();
        } else {
            log.error("AuthException class: {}", authException.getClass().getName());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,authException.getMessage());
        }
    }
}
