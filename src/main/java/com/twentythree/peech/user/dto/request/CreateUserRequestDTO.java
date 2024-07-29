package com.twentythree.peech.user.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.twentythree.peech.user.AuthorizationServer;
import com.twentythree.peech.user.UserGender;
import com.twentythree.peech.user.UserRole;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateUserRequestDTO {

    private String deviceId;
    private String socialId;
    private AuthorizationServer authorizationServer;
    private String firstName;
    private String lastName;
    private LocalDate birth;
    private UserGender gender;
    private String email;
    private String nickName;
    private UserRole userRole;

    @JsonCreator
    public CreateUserRequestDTO(String deviceId) {
        this.deviceId = deviceId;
    }
}
