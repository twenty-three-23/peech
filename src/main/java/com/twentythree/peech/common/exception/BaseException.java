package com.twentythree.peech.common.exception;

import com.twentythree.peech.common.dto.UserAlreadyExistErrorVO;
import com.twentythree.peech.common.dto.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaseException {

    Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<UserAlreadyExistErrorVO> UserAlreadyExistExceptionHandler(UserAlreadyExistException e) {
        log.error("이미 가입된 유저 에러 발생", e);
        UserAlreadyExistErrorVO alreadyExistErrorVO = new UserAlreadyExistErrorVO(e.getMessage());
        return new ResponseEntity<>(alreadyExistErrorVO, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> baseExceptionHandler(Exception e) {
        log.error("에러 발생", e);
        ErrorDTO errorDTO = new ErrorDTO(e.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessTokenExpiredException.class)
    public ResponseEntity<String> handleExpiredJWTTokenException(AccessTokenExpiredException e) {
        log.error("access 토큰 만료 에러 발생", e);
        // 410번 에러 코드로 응답
        return new ResponseEntity<>(e.getMessage(), HttpStatus.GONE);
    }

    @ExceptionHandler(RefreshTokenExpiredException.class)
    public ResponseEntity<String> handleExpiredJWTTokenException(RefreshTokenExpiredException e) {
        log.error("refresh 토큰 만료 에러 발생", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
