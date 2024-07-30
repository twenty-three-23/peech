package com.twentythree.peech.user.domain;

import com.twentythree.peech.user.AuthorizationIdentifier;
import com.twentythree.peech.user.SignUpFinished;
import com.twentythree.peech.user.UserGender;
import com.twentythree.peech.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

import static com.twentythree.peech.usagetime.constant.UsageConstantValue.DEFAULT_USAGE_TIME;

@Getter
@AllArgsConstructor
public class UserDomain {

    private Long userId;
    private AuthorizationIdentifier authorizationIdentifier;
    private String firstName;
    private String lastName;
    private LocalDate birth;
    private UserGender gender;
    private String email;
    private String nickName;
    private UserRole role;
    private SignUpFinished signUpFinished;

    private Long usageTime;
    private Long remainingTime;

    private UserDomain(AuthorizationIdentifier authorizationIdentifier, String firstName, String lastName,
                       LocalDate birth, UserGender gender, String email,
                       String nickName, UserRole role, Long usageTime, Long remainingTime) {
        this.authorizationIdentifier = authorizationIdentifier;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birth = birth;
        this.gender = gender;
        this.email = email;
        this.nickName = nickName;
        this.role = role;
        this.usageTime = usageTime;
        this.remainingTime = remainingTime;
    }

    private UserDomain( AuthorizationIdentifier authorizationIdentifier, String email, UserRole role, Long usageTime, Long remainingTime, SignUpFinished signUpFinished) {
        this.authorizationIdentifier = authorizationIdentifier;
        this.email = email;
        this.role = role;
        this.usageTime = usageTime;
        this.remainingTime = remainingTime;
        this.signUpFinished = signUpFinished;
    }

    public static UserDomain of(AuthorizationIdentifier authorizationIdentifier, String firstName,
                                String lastName, LocalDate birth, UserGender gender,
                                String email, String nickName){
        return new UserDomain(authorizationIdentifier, firstName, lastName, birth, gender, email, nickName, UserRole.COMMON, DEFAULT_USAGE_TIME, DEFAULT_USAGE_TIME);
    }

    public static UserDomain ofEmail(AuthorizationIdentifier authorizationIdentifier, String email, UserRole role, Long usageTime, Long remainingTime, SignUpFinished signUpFinished){
        return new UserDomain(authorizationIdentifier, email, role, usageTime, remainingTime, signUpFinished);
    }
}
