package com.twentythree.peech.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RequestInformationDTO {
    private String jwtToken;
    private String requestURI;
}
