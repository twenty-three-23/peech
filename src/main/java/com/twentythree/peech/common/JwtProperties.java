package com.twentythree.peech.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtProperties {

    @Value("${jwt.secret.key}")
    private String secretString;

    @Value("${jwt.access.key}")
    private String accessString;

    @Value("${jwt.refresh.key}")
    private String refreshString;
}
