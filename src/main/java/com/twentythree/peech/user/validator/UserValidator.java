package com.twentythree.peech.user.validator;

import com.twentythree.peech.user.entity.AuthorizationIdentifier;

public interface UserValidator {
    boolean NickNameIsNotDuplicated(String nickName);
    boolean existUser(AuthorizationIdentifier authorizationIdentifier);
    boolean notExistUser(AuthorizationIdentifier authorizationIdentifier);
}
