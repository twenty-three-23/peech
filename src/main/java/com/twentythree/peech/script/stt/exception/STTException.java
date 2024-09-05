package com.twentythree.peech.script.stt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class STTException extends RuntimeException{

    private final STTExceptionCode sttExceptionCode;

    public String getErrorMessage() {
        return sttExceptionCode.getMessage();
    }
}
