package com.twentythree.peech.user.validator;

public interface UserValidator {
    boolean NickNameIsNotDuplicated(String nickName);
    boolean existUserByEmail(String email);
    boolean notExistUserByEmail(String email);
}
