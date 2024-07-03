package com.twentythree.peech.user.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

@Data
public class UserIdTokenResponseDTO {
    private String token;

    @JsonCreator
    public UserIdTokenResponseDTO(String token) {
        this.token = token;
    }
}
