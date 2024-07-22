package com.twentythree.peech.common.exception;

import com.twentythree.peech.common.dto.UserAlreadyExistErrorVO;
import com.twentythree.peech.common.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BaseException {

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
}
