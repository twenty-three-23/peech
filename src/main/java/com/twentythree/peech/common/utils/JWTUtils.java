package com.twentythree.peech.common.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

@Component
public class JWTUtils {

    // constructor가 초기화 되고 값이 주입이 되기 떄문에 constructor의 secretString은 null이 들어 감
    @Value("${jwt.secret.key}")
    private String secretString;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretString = Base64.getEncoder().encodeToString(secretString.getBytes());
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
    }

    public String createJWT(Long userId) {
        return Jwts.builder().
                header().
                and().
                subject("LoginToken").
                claim("userId", userId).
                signWith(secretKey, Jwts.SIG.HS256).
                compact();
    }

}
