package com.twentythree.peech.user.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

@Data
public class CreateUserResponseDTO {
    private String token;

    @JsonCreator
    public CreateUserResponseDTO(String token) {
        this.token = token;
    }
}
