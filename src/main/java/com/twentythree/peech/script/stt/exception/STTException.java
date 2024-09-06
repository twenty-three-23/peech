package com.twentythree.peech.script.stt.exception;

import lombok.Getter;

@Getter
public class STTException extends RuntimeException{

    private final STTExceptionCode sttExceptionCode;

    public STTException(STTExceptionCode sttException){
        super(sttException.getMessage());
        this.sttExceptionCode = sttException;
    }

    public String getErrorMessage() {
        return sttExceptionCode.getMessage();
    }
}
