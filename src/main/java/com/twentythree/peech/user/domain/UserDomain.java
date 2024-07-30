package com.twentythree.peech.user.domain;

import com.twentythree.peech.user.AuthorizationIdentifier;
import com.twentythree.peech.user.UserGender;
import com.twentythree.peech.user.UserRole;
import com.twentythree.peech.user.UserStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

import static com.twentythree.peech.usagetime.constant.UsageConstantValue.DEFAULT_USAGE_TIME;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
    private UserStatus userStatus;

    private Long usageTime;
    private Long remainingTime;
    private LocalDate deleteAt;

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

    public static UserDomain of(Long userId, AuthorizationIdentifier authorizationIdentifier, String firstName,
                                String lastName, LocalDate birth, UserGender gender,
                                String email, String nickName, UserRole role, UserStatus userStatus, Long usageTime, Long remainingTime, LocalDate deleteAt){
        return new UserDomain(userId, authorizationIdentifier, firstName, lastName, birth, gender, email, nickName, role, userStatus, usageTime, remainingTime, deleteAt);
    }

    public static UserDomain ofCreateUser(AuthorizationIdentifier authorizationIdentifier, String firstName,
                                          String lastName, LocalDate birth, UserGender gender,
                                          String email, String nickName){
        return new UserDomain(authorizationIdentifier, firstName, lastName, birth, gender, email, nickName, UserRole.COMMON, DEFAULT_USAGE_TIME, DEFAULT_USAGE_TIME);
    }


    public void changeUserStatusToDelete() {
        this.userStatus = UserStatus.DELETE;
    }

    public LocalDate setDeleteAt(LocalDate deleteAt) {
        this.deleteAt = deleteAt;
        return this.deleteAt;
    }
}
