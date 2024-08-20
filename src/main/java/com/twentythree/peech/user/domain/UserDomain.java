package com.twentythree.peech.user.domain;

import com.twentythree.peech.user.value.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

import static com.twentythree.peech.usagetime.constant.UsageConstantValue.DEFAULT_USAGE_TIME;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDomain {

    private Long userId;
    private AuthorizationServer authorizationServer;
    private String firstName;
    private String lastName;
    private LocalDate birth;
    private UserGender gender;
    private String email;
    private String nickName;
    private UserRole role;
    private SignUpFinished signUpFinished;
    private UserStatus userStatus;

    private Long usageTime;
    private Long remainingTime;
    private LocalDate deleteAt;

    private UserDomain(AuthorizationServer authorizationServer, String firstName, String lastName,
                       LocalDate birth, UserGender gender, String email,
                       String nickName, UserRole role, Long usageTime, Long remainingTime) {
        this.authorizationServer = authorizationServer;
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

    private UserDomain( AuthorizationServer authorizationServer , String email, UserRole role, Long usageTime, Long remainingTime, SignUpFinished signUpFinished) {
        this.authorizationServer = authorizationServer;
        this.email = email;
        this.role = role;
        this.usageTime = usageTime;
        this.remainingTime = remainingTime;
        this.signUpFinished = signUpFinished;
    }

    public static UserDomain of(Long userId, AuthorizationServer authorizationServer, String firstName,
                                String lastName, LocalDate birth, UserGender gender,
                                String email, String nickName, UserRole role, UserStatus userStatus, Long usageTime, Long remainingTime, LocalDate deleteAt, SignUpFinished signUpFinished ){
        return new UserDomain(userId, authorizationServer, firstName, lastName, birth, gender, email, nickName, role, signUpFinished, userStatus, usageTime, remainingTime, deleteAt);
    }

    public static UserDomain ofCreateUser(AuthorizationServer authorizationServer, String firstName,
                                          String lastName, LocalDate birth, UserGender gender,
                                          String email, String nickName){
        return new UserDomain(authorizationServer, firstName, lastName, birth, gender, email, nickName, UserRole.ROLE_COMMON, DEFAULT_USAGE_TIME, DEFAULT_USAGE_TIME);
    }


    public void changeUserStatusToDelete() {
        this.userStatus = UserStatus.DELETE;
    }

    public LocalDate setDeleteAt(LocalDate deleteAt) {
        this.deleteAt = deleteAt;
        return this.deleteAt;
    }

    // Q 1. 이런 식으로 도메인에 수정 코드를 넣고 creator에서 함수를 생성해서 하는게 맞는가, 아니면 이 메소드를 바로 호출하는게 옮은가
    //  나의 생각으로는 바로 호출하는 게 맞다고 생각이 드는데.. 이유는 모르겠지만 느낌이 뺴야할 것 같은 느낌쓰
    public void setUser(AuthorizationServer authorizationServer, String firstName, String lastName, LocalDate birth, UserGender gender, String email, String nickName, UserRole role, UserStatus userStatus, SignUpFinished signUpFinished, LocalDate deleteAt) {
        this. authorizationServer = authorizationServer;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birth = birth;
        this.gender = gender;
        this.email = email;
        this.nickName = nickName;
        this.role = role;
        this.userStatus = userStatus;
        this.signUpFinished = signUpFinished;
        this.deleteAt = deleteAt;
    }

    public static UserDomain ofEmail(AuthorizationServer authorizationServer, String email, UserRole role, Long usageTime, Long remainingTime, SignUpFinished signUpFinished){
        return new UserDomain(authorizationServer, email, role, usageTime, remainingTime, signUpFinished);
    }
}
