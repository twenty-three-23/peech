package com.twentythree.peech.user.validator;

import com.twentythree.peech.user.dto.KakaoAccount;

public interface UserValidator {
    boolean NickNameIsNotDuplicated(String nickName);
    boolean existUserByEmail(String email);
    boolean notExistUserByEmail(String email);
    boolean kakaoEmailValid(KakaoAccount kakaoAccount);
    boolean singedUpFinishedUser(Long userId);
    boolean userIsNotDeleted(Long userId);
}
