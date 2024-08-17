package com.twentythree.peech.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginBySocial {
    private String accessToken;
    private String refreshToken;
    private Integer responseCode;
}
