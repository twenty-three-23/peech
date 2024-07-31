package com.twentythree.peech.user.domain;

import com.twentythree.peech.usagetime.constant.UsageConstantValue;
import com.twentythree.peech.user.entity.AuthorizationIdentifier;
import com.twentythree.peech.user.value.*;
import com.twentythree.peech.user.validator.UserValidator;
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

    public UserDomain completeUser(UserDomain userDomain,  AuthorizationIdentifier authorizationIdentifier, String firstName, String lastName, LocalDate birth, UserGender gender, String email, String nickName, UserRole role, UserStatus userStatus, SignUpFinished signUpFinished, LocalDate deleteAt) {
        if (userValidator.NickNameIsNotDuplicated(nickName) && userDomain.getUserStatus() != UserStatus.DELETE && userStatus != UserStatus.DELETE) {
            userDomain.setUser(authorizationIdentifier, firstName, lastName, birth, gender, email, nickName, role, userStatus, signUpFinished, deleteAt);
        } else {
            if (userDomain.getUserStatus() == UserStatus.DELETE || userStatus == UserStatus.DELETE) {
                throw new IllegalArgumentException("삭제된 유저는 정보를 변경 할 수 없습니다.");
            } else {
                throw new IllegalArgumentException("닉네임이 중복 되었습니다.");
            }
        }
        return userDomain;
    }
}
