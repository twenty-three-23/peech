package com.twentythree.peech.user.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

@Data
public class UserIdTokenResponseDTO {
    private String accessToken;
    private String refreshToken;

    @JsonCreator
    public UserIdTokenResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
