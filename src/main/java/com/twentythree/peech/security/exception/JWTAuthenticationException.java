package com.twentythree.peech.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

public class JWTAuthenticationException extends AuthenticationException {

    private final LoginExceptionCode loginExceptionCode;

    public JWTAuthenticationException(LoginExceptionCode loginExceptionCode) {
        super(loginExceptionCode.getMessage());
        this.loginExceptionCode = loginExceptionCode;
    }

    public HttpStatus getStatus() {
        return loginExceptionCode.getStatus();
    }
}
