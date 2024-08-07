package com.twentythree.peech.common.exception;

import io.jsonwebtoken.ExpiredJwtException;

public class RefreshTokenExpiredException extends RuntimeException{
    public RefreshTokenExpiredException(String message) {
        super(message);
    }
}
