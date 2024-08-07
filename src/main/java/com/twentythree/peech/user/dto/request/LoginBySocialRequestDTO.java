package com.twentythree.peech.user.dto.request;

import com.twentythree.peech.user.value.AuthorizationServer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginBySocialRequestDTO {
    private String socialToken;
    private AuthorizationServer authorizationServer;
}
