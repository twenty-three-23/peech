package com.twentythree.peech.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twentythree.peech.common.JwtProperties;
import com.twentythree.peech.common.exception.AccessTokenExpiredException;
import com.twentythree.peech.common.exception.RefreshTokenExpiredException;
import com.twentythree.peech.user.dto.IdentityToken;
import com.twentythree.peech.user.dto.IdentityTokenHeader;
import com.twentythree.peech.user.dto.IdentityTokenPayload;
import com.twentythree.peech.user.value.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JWTUtils {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
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

    public Jws<Claims> parseAccessToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(accessKey)
                .build()
                .parseSignedClaims(token);

        return claimsJws;
    }

    public Jws<Claims> parseRefreshToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .verifyWith(refreshKey)
                .build()
                .parseSignedClaims(token);

        return claimsJws;
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public Claims validateAccessToken(String token) {
        try {
            Jws<Claims> claimsJws = parseAccessToken(token);
            return claimsJws.getPayload();
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw new AccessTokenExpiredException("AccessToken이 만료되었습니다. refreshToken으로 재요청해주세요.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT Token is empty", e);
        }
        return null;
    }

    public Claims validateRefreshToken(String token) {
        try {
            Jws<Claims> claimsJws = parseRefreshToken(token);
            return claimsJws.getPayload();
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw new RefreshTokenExpiredException("RefreshToken이 만료되었습니다. 다시 로그인해 주세요.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT Token is empty", e);
        }
        return null;
    }
  
    public IdentityToken decodeIdentityToken(String token) {
        try {

            String[] splitToken = token.split("\\.");

            String header = splitToken[0];
            String payload = splitToken[1];

            String tokenHeader = new String(Decoders.BASE64.decode(header));
            String tokenPayload = new String(Decoders.BASE64.decode(payload));


            ObjectMapper objectMapper = new ObjectMapper();

            IdentityTokenHeader identityTokenHeader = objectMapper.readValue(tokenHeader, IdentityTokenHeader.class);
            IdentityTokenPayload identityTokenPayload = objectMapper.readValue(tokenPayload, IdentityTokenPayload.class);


            return new IdentityToken(identityTokenHeader, identityTokenPayload);
        } catch (Exception e) {
            throw new IllegalArgumentException("error", e);
        }
    }
}
