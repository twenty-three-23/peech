package com.twentythree.peech.user.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

@Data
public class CreateUserResponseDTO {
    private Long userId;
    @JsonCreator
    public CreateUserResponseDTO(Long userId) {
        this.userId = userId;
    }
}
