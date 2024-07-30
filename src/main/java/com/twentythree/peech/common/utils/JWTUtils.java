package com.twentythree.peech.common.utils;

import com.twentythree.peech.common.JwtProperties;
import com.twentythree.peech.user.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JWTUtils {

    private final JwtProperties jwtProperties;

    private String secretString;
    private String accessString;
    private String refreshString;

    private SecretKey secretKey;
    private SecretKey accessKey;
    private SecretKey refreshKey;

    private Date accessExpirationDate;
    private Date refreshExpirationDate;

    private final Long accessExpiration = 30L;
    private final Long refreshExpiration = 60L;


    @PostConstruct
    public void init() {

        secretString = jwtProperties.getSecretString();
        accessString = jwtProperties.getAccessString();
        refreshString = jwtProperties.getRefreshString();

        secretString = Base64.getEncoder().encodeToString(secretString.getBytes());
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));

        accessString = Base64.getEncoder().encodeToString(secretString.getBytes());
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessString));

        refreshString = Base64.getEncoder().encodeToString(secretString.getBytes());
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshString));

        Date today = new Date();
        this.accessExpirationDate = new Date(today.getTime() + (1000 * 60 * 60 * 24 * accessExpiration));
        this.refreshExpirationDate = new Date(today.getTime() + (1000 * 60 * 60 * 24 * refreshExpiration));
    }


    public String createJWT(Long userId) {
        return Jwts.builder().
                header().
                and().
                subject("Token").
                claim("userId", userId).
                signWith(secretKey, Jwts.SIG.HS256).
                compact();
    }

    public String createAccessToken(Long userId, UserRole userRole) {
        return Jwts.builder().
                header().
                and().
                subject("AccessToken").
                claim("userId", userId).
                claim("userRole", userRole).
                expiration(accessExpirationDate).
                signWith(accessKey, Jwts.SIG.HS256).
                compact();
    }

    public String createRefreshToken(Long userId, UserRole userRole) {
        return Jwts.builder().
                header().
                and().
                subject("RefreshToken").
                claim("userId", userId).
                claim("userRole", userRole).
                expiration(refreshExpirationDate).
                signWith(refreshKey, Jwts.SIG.HS256).
                compact();
    }

    public Jws<Claims> parseJWT(String token) {

        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);

        return claimsJws;
    }

    public Jws<Claims> parseAccessToken(String token, Long userId) {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(accessKey)
                .build()
                .parseSignedClaims(token);

        return claimsJws;
    }

    public Jws<Claims> parseRefreshToken(String token, Long userId) {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(refreshKey)
                .build()
                .parseSignedClaims(token);

        return claimsJws;
    }

}
