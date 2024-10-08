package com.twentythree.peech.common.exception;

import com.twentythree.peech.common.dto.UserAlreadyExistErrorVO;
import com.twentythree.peech.common.dto.ErrorDTO;
import com.twentythree.peech.script.stt.exception.STTException;
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

    @ExceptionHandler(Unauthorized.class)
    public ResponseEntity<Unauthorized> unauthorizedExceptionHandler(Unauthorized e) {
        log.error(e.getMessage(), e);
        Unauthorized unauthorized = new Unauthorized(e.getMessage());
        return new ResponseEntity<>(unauthorized, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> baseExceptionHandler(Exception e) {
        log.error("에러 발생", e);
        ErrorDTO errorDTO = new ErrorDTO(e.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(STTException.class)
    public ResponseEntity<ErrorDTO> sttExceptionHandler(STTException e) {
        Throwable cause = e.getCause();

        if(cause != null){
            log.error("STT 에러 예외 발생 원인 = {} ", cause.getMessage());
            log.error("STT 에러 발생 메시지 = {}", e.getMessage());

            return new ResponseEntity<>(new ErrorDTO(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);

        } else {
            log.error("잘못된 유저 사용으로 발생한 에러");
            log.error("STT 에러 발생 메시지 = {}", e.getMessage());
        }
        ErrorDTO errorDTO = new ErrorDTO(e.getMessage());
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
}
