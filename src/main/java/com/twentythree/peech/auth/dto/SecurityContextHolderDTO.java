package com.twentythree.peech.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SecurityContextHolderDTO {
    Long userId;
    String funnel;
}
