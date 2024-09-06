package com.twentythree.peech.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JWTAuthentication {
    Long userId;
    String funnel;
}
