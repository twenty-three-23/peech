package com.twentythree.peech.common.exception;

import lombok.Data;

@Data
public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
