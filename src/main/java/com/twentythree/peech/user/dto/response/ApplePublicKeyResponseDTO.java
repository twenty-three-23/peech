package com.twentythree.peech.user.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.twentythree.peech.user.dto.ApplePublicKey;
import com.twentythree.peech.user.dto.IdentityTokenHeader;
import io.jsonwebtoken.io.Decoders;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.List;

@Slf4j
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApplePublicKeyResponseDTO {

    @JsonProperty("keys")
    private List<ApplePublicKey> applePublicKeys;

    public PublicKey getApplePublicKeyKey(IdentityTokenHeader identityTokenHeader) {
        String alg = identityTokenHeader.getAlg();
        String kid = identityTokenHeader.getKid();

        for (ApplePublicKey publicKey : applePublicKeys) {
            if (publicKey.getKid().equals(kid) && publicKey.getAlg().equals(alg)) {

                byte[] n = Decoders.BASE64URL.decode(publicKey.getN());
                byte[] e = Decoders.BASE64URL.decode(publicKey.getE());
                RSAPublicKeySpec publicKeySpec =
                        new RSAPublicKeySpec(new BigInteger(1, n), new BigInteger(1, e));

                try {
                    KeyFactory keyFactory = KeyFactory.getInstance(publicKey.getKty());
                    return keyFactory.generatePublic(publicKeySpec);
                } catch (NoSuchAlgorithmException | InvalidKeySpecException exception) {
                    throw new RuntimeException("응답 받은 Apple Public Key로 PublicKey를 생성할 수 없습니다.");
                }
            }
        }

        throw new RuntimeException("Token을 검증할 수 없습니다.");
    }
}
