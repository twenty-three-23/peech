package com.twentythree.peech.usagetime.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class CheckRemainingTimeResponseDTO {
    private String message;
    private boolean success;
}
