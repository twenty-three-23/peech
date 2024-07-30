package com.twentythree.peech.user.domain;

import com.twentythree.peech.usagetime.constant.UsageConstantValue;
import com.twentythree.peech.user.AuthorizationIdentifier;
import com.twentythree.peech.user.SignUpFinished;
import com.twentythree.peech.user.UserRole;
import com.twentythree.peech.user.validator.UserValidator;
import com.twentythree.peech.user.UserGender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@RequiredArgsConstructor
@Component
public class UserCreator {

    private final UserValidator userValidator;

    public UserDomain createUser(AuthorizationIdentifier authorizationIdentifier, String firstName, String lastName, LocalDate birth, String email, UserGender gender, String nickName) {
        if ( userValidator.NickNameIsNotDuplicated(nickName) ) {
            return UserDomain.ofCreateUser( authorizationIdentifier,  firstName, lastName, birth, gender, email, nickName);
        } else {
            throw new IllegalArgumentException("닉네임이 중복 되었습니다 : " + nickName);
        }
    }

    public UserDomain createUserByEmail(AuthorizationIdentifier authorizationIdentifier, String email, SignUpFinished signUpFinished) {
        return UserDomain.ofEmail(authorizationIdentifier, email, UserRole.ROLE_COMMON, UsageConstantValue.DEFAULT_USAGE_TIME, UsageConstantValue.DEFAULT_USAGE_TIME, signUpFinished);
    }
}
