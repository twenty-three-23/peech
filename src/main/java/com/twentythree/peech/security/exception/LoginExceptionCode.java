package com.twentythree.peech.security.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum LoginExceptionCode{

    LOGIN_EXCEPTION_CODE(HttpStatus.UNAUTHORIZED, "유효하지않은 토큰입니다."),
    SIGNUP_FINISHED_NOT_YET(HttpStatus.LENGTH_REQUIRED, "아직 가입이 완료되지 않았습니다."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.GONE, "access 토큰이 만료되었습니다. refresh 토큰으로 로그인 해주세요."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "refresh 토큰이 만료되었습니다. 다시 로그인 해주세요."),
    DELETE_USER(HttpStatus.BAD_REQUEST, "탈퇴한 회원입니다.");


    private final HttpStatus status;
    private final String message;

    LoginExceptionCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
