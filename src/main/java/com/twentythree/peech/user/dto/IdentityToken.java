package com.twentythree.peech.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IdentityToken {

    private IdentityTokenHeader identityTokenHeader;
    private IdentityTokenPayload identityTokenPayload;

    public boolean isVerify(List<ApplePublicKey> publicKeys) {
        String alg = identityTokenHeader.getAlg();
        String kid = identityTokenHeader.getKid();

        for (ApplePublicKey publicKey : publicKeys) {
            if (publicKey.getKid().equals(alg) && publicKey.getAlg().equals(kid)) {
                return true;
            }
        }
        return false;
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
