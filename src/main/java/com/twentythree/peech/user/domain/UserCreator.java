package com.twentythree.peech.user.domain;

import com.twentythree.peech.usagetime.constant.UsageConstantValue;
import com.twentythree.peech.user.value.*;
import com.twentythree.peech.user.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;


@RequiredArgsConstructor
@Component
public class UserCreator {

    private final UserValidator userValidator;

    public UserDomain createUser(AuthorizationServer authorizationServer, String firstName, String lastName, LocalDate birth, String email, UserGender gender, String nickName) {
        if ( userValidator.NickNameIsNotDuplicated(nickName) ) {
            return UserDomain.ofCreateUser( authorizationServer,  firstName, lastName, birth, gender, email, nickName);
        } else {
            throw new IllegalArgumentException("닉네임이 중복 되었습니다 : " + nickName);
        }
    }

    public UserDomain createUserByEmail(AuthorizationServer authorizationServer, String email, SignUpFinished signUpFinished) {
        return UserDomain.ofEmail(authorizationServer, email, UserRole.ROLE_COMMON, UsageConstantValue.DEFAULT_USAGE_TIME, UsageConstantValue.DEFAULT_USAGE_TIME, signUpFinished);
    }

    public UserDomain completeUser(UserDomain userDomain,  AuthorizationServer authorizationServer, String firstName, String lastName, LocalDate birth, UserGender gender, String email, String nickName, UserRole role, UserStatus userStatus, SignUpFinished signUpFinished, LocalDateTime deleteAt) {
        if (userValidator.NickNameIsNotDuplicated(nickName) && userDomain.getUserStatus() != UserStatus.DELETE && userStatus != UserStatus.DELETE) {
            userDomain.setUser(authorizationServer, firstName, lastName, birth, gender, email, nickName, role, userStatus, signUpFinished, deleteAt);
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
