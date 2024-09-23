package com.twentythree.peech.user.dto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.security.PublicKey;
import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IdentityToken {

    private IdentityTokenHeader identityTokenHeader;
    private IdentityTokenPayload identityTokenPayload;

    public boolean isVerify(String jwt, PublicKey publicKey) {

        try {
            Jws<Claims> jwsClaims = Jwts.parser()
                    .setSigningKey(publicKey) // 공개 키 설정
                    .build()
                    .parseClaimsJws(jwt);
        } catch (Exception e) {
            throw new RuntimeException("토큰이 올바르지 못합니다.");
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentityToken that = (IdentityToken) o;
        return Objects.equals(identityTokenHeader, that.identityTokenHeader) && Objects.equals(identityTokenPayload, that.identityTokenPayload);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identityTokenHeader, identityTokenPayload);
    }
}
