package com.twentythree.peech.user.dto.request;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;

@Data
public class CreateUserRequestDTO {
    private String deviceId;

    @JsonCreator
    public CreateUserRequestDTO(String deviceId) {
        this.deviceId = deviceId;
    }
}
