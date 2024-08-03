package com.twentythree.peech.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoAccount {
    private String email;

    @JsonProperty("is_email_verified")
    private boolean isEmailVerified;

    @JsonProperty("is_email_valid")
    private boolean isEmailValid;
}
