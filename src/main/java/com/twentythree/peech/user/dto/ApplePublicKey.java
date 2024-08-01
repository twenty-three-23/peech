package com.twentythree.peech.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplePublicKey {
    private String alg;
    private String e;
    private String kid;
    private String kty;
    private String n;
    private String use;
}
